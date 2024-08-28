package com.example.fitnessapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.model.Plans
import com.example.fitnessapp.service.roomdb.ExerciseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PlanListViewModel @Inject constructor(application: Application) : BaseViewModel(application){

    var plans = mutableStateOf<List<Plans>>(emptyList())

    init {
        loadPlansSQL()
    }

    //Private olma ihtimali
    fun loadPlansSQL(){

        viewModelScope.launch {
            val dao = ExerciseDatabase(getApplication()).plansDAO()

            val planList = withContext(Dispatchers.IO){
                dao.getAllPlans()
            }

            withContext(Dispatchers.Main){
                plans.value = planList
            }
        }
    }
}