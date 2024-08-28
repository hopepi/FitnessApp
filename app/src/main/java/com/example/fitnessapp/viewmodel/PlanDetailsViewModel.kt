package com.example.fitnessapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.model.Exercise
import com.example.fitnessapp.model.PlanExercise
import com.example.fitnessapp.model.Plans
import com.example.fitnessapp.service.roomdb.ExerciseDatabase
import com.example.fitnessapp.util.calculateDelayInMinutes
import com.example.fitnessapp.util.parseNotificationTime
import com.example.fitnessapp.util.scheduleNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(application: Application) : BaseViewModel(application) {
    var planDetail = mutableStateOf<Plans?>(null)
    var planExerciseDetail = mutableStateOf<List<PlanExercise>>(emptyList())
    var exerciseDetail = mutableStateOf<List<Exercise>>(emptyList())
    var exerciseQuantity = mutableStateOf<List<Int>>(emptyList())

    private var exercise: MutableList<Exercise> = mutableListOf()
    private var quantity: MutableList<Int> = mutableListOf()

    fun loadPlanDetailRoom(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = ExerciseDatabase(getApplication()).plansDAO()
            val plan = dao.getPlan(id)
            val exercises = dao.getExercisesForPlan(plan.planId)

            withContext(Dispatchers.Main) {
                planDetail.value = plan
                planExerciseDetail.value = exercises
                loadPlanExerciseDetailGetRoom(exercises, id)
            }
        }
    }

    fun updateAvailability(id: Int, availability: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = ExerciseDatabase(getApplication()).plansDAO()
            dao.updatePlanAvailability(id, availability)
            loadPlanDetailRoom(id)
        }
    }

    fun deletePlan(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = ExerciseDatabase(getApplication()).plansDAO()
            val plan = dao.getPlan(id)
            dao.deletePlan(plan)
        }
    }

    private fun loadPlanExerciseDetailGetRoom(planDetail: List<PlanExercise>, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            exercise = mutableListOf()
            quantity = mutableListOf()

            planDetail.forEach { planExercise ->
                val getDetailExercise = ExerciseDatabase(getApplication()).exerciseDAO().getExercise(planExercise.exerciseId)
                val getDetailQuantity = ExerciseDatabase(getApplication()).plansDAO().getExercisesForPlanQuantity(id, planExercise.exerciseId)
                quantity.add(getDetailQuantity.last().quantity)
                exercise.add(getDetailExercise)
            }

            withContext(Dispatchers.Main) {
                exerciseDetail.value = exercise.toList() // Listeyi kopyalayarak atama yapıyoruz
                exerciseQuantity.value = quantity.toList() // Listeyi kopyalayarak atama yapıyoruz
            }
        }
    }

    fun alarmSet(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val plan = ExerciseDatabase(getApplication()).plansDAO().getPlan(id)
            withContext(Dispatchers.Main) {
                val scheduleTime = parseNotificationTime(plan.notificationTime)
                if (scheduleTime != null) {
                    val delayMinutes = calculateDelayInMinutes(scheduleTime)
                    if (delayMinutes > 0) {
                        scheduleNotification(getApplication(), delayMinutes)
                    } else {
                        scheduleNotification(getApplication(), 0)
                    }
                }
            }
        }
    }
}
