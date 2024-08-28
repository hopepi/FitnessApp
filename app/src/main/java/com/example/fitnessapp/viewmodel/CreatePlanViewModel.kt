package com.example.fitnessapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.model.Exercise
import com.example.fitnessapp.model.PlanExercise
import com.example.fitnessapp.model.Plans
import com.example.fitnessapp.repository.ExerciseRepository
import com.example.fitnessapp.service.roomdb.ExerciseDatabase
import com.example.fitnessapp.util.DataStoreObject
import com.example.fitnessapp.util.Resource
import com.example.fitnessapp.util.calculateDelayInMinutes
import com.example.fitnessapp.util.parseNotificationTime
import com.example.fitnessapp.util.scheduleNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CreatePlanViewModel @Inject constructor(
    application: Application,
    private val repository: ExerciseRepository) : BaseViewModel(application){

    val exerciseList = mutableStateOf<List<Exercise>>(emptyList())
    val exerciseLoading = mutableStateOf(false)
    val exerciseErrorMessage = mutableStateOf("")

    var savePlansId : Long? = null
    var selectExercises = mutableStateOf<List<Int>>(emptyList())

    var selectExerciseandQuantity = mutableStateOf<Map<Exercise,Int>>(emptyMap())

    private val updateTime = 10 * 60 * 1000 * 1000 * 1000L

    private val dataStoreObject = DataStoreObject(getApplication())


    private fun getInternet(){
        exerciseLoading.value = true
        exerciseErrorMessage.value = ""

        viewModelScope.launch (Dispatchers.IO){
            val result = repository.getExerciseList()

            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success ->{
                        exerciseList.value = result.data?.exercises ?: emptyList()
                        withContext(Dispatchers.IO){
                            saveRoomDatabase(result.data?.exercises ?: emptyList())
                        }
                        exerciseLoading.value = false
                    }
                    is Resource.Error -> {
                        exerciseErrorMessage.value = result.message!!
                        exerciseLoading.value = false
                    }
                    is Resource.Loading -> {
                        exerciseLoading.value = true
                    }
                }
            }
        }
    }

    private fun getRoomDatabase(){
        exerciseLoading.value = true
        viewModelScope.launch (Dispatchers.IO) {
            val result = ExerciseDatabase(getApplication()).exerciseDAO().getAllExercise()
            withContext(Dispatchers.Main){
                exerciseLoading.value = false
                exerciseList.value = result
            }
        }
    }

    private fun saveRoomDatabase(exerciseListPar : List<Exercise>){

        viewModelScope.launch (Dispatchers.IO){
            val dao =ExerciseDatabase(getApplication()).exerciseDAO()
            dao.deleteAllExercise()
            if (exerciseListPar.isNotEmpty()){
                val idList = dao.insertAll(*exerciseListPar.toTypedArray())
                var i = 0
                while (i < exerciseListPar.size){
                    exerciseListPar[i].id = idList[i].toInt()
                    i += 1
                }
                exerciseList.value = exerciseListPar
            }
        }
        //Zamanı kaydetme
        dataStoreObject.saveTime(System.nanoTime())
    }


    fun refreshData(){
        val saveTime = dataStoreObject.getTime()

        if (saveTime != null && saveTime != 0L && System.nanoTime() - saveTime < updateTime){
            getRoomDatabase()
        }
        else{
            refreshInternet()
        }
    }

    fun refreshInternet(){
        getInternet()
    }

    private suspend fun savePlan(planName : String , notificationTime : String) : Long{
        val dao = ExerciseDatabase(getApplication()).plansDAO()
        return dao.insertPlan(Plans(planName,notificationTime,true))
    }

    fun savePLanAndExercise(planName : String , notificationTime : String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                savePlansId = savePlan(planName, notificationTime)
                savePlansId?.let { plansId->
                    val dao = ExerciseDatabase(getApplication()).plansDAO()

                    selectExerciseandQuantity.value.forEach { exercise ->
                        dao.insertPlanExercise(
                            PlanExercise(plansId.toInt(), exercise.key.id, exercise.value)
                        )
                    }
                }
                val scheduleTime = parseNotificationTime(notificationTime)
                if (scheduleTime != null) {
                    val delayMinutes = calculateDelayInMinutes(scheduleTime)
                    if (delayMinutes > 0) {
                        scheduleNotification(getApplication(), delayMinutes)
                    } else {
                        // Geçmiş zaman, hemen bildirim gönder
                        scheduleNotification(getApplication(), 0)
                    }
                }

                selectExercises.value = emptyList()
            }catch (e : Exception){
             //HATA
            }
        }

    }


}