package com.example.fitnessapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fitnessapp.model.Exercise
import com.example.fitnessapp.viewmodel.PlanDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    viewModel: PlanDetailsViewModel = hiltViewModel(),
    navController: NavController,
    id : Int
) {
    val planDetail by viewModel.planDetail
    val planDetailExercise by viewModel.exerciseDetail
    val planExerciseQuantity by viewModel.exerciseQuantity

    LaunchedEffect (key1 = Unit){
        viewModel.loadPlanDetailRoom(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Boş bırakılabilir ya da başlık eklenebilir */ },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.deletePlan(id).apply {
                            navController.navigateUp()
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                    IconButton(onClick = {
                        viewModel.alarmSet(id)
                    }) {
                        Icon(Icons.Default.Call, contentDescription = "Alarm")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            planDetail?.let {
                Text(text = planDetail!!.planName)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = planDetail!!.notificationTime)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Avability")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = planDetail!!.availability,
                        onCheckedChange = { isChecked ->
                            viewModel.updateAvailability(it.planId, isChecked)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                val exerciseWithQuantity = planDetailExercise.mapIndexed { index, exercise ->
                    val quantity = planExerciseQuantity.getOrNull(index) ?: 0
                    exercise to quantity
                }
                LazyColumn {
                    items(exerciseWithQuantity) { (exercise, quantity) ->
                        Column {
                            Text(text = "Name: ${exercise.name}")
                            Divider()
                            Text(text = "Description: ${exercise.description}")
                            Divider()
                            Text(text = "Recommendation: ${exercise.recommendation}")
                            Divider()
                            Text(text = "Quantity: $quantity")
                            Divider()
                        }
                    }
                }
            }
        }
    }
}