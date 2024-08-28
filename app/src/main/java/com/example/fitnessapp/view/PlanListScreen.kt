package com.example.fitnessapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fitnessapp.model.Plans
import com.example.fitnessapp.viewmodel.PlanListViewModel

@Composable
fun PlanListScreen(
    viewModel: PlanListViewModel = hiltViewModel(),
    navController: NavController
) {
    val plans by viewModel.plans

    LaunchedEffect (key1 = Unit){
        viewModel.loadPlansSQL()
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("createPlanScreen") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Plan")
            }
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            LazyColumn {
                items(plans) { plan ->
                    PlanItem(
                        plan = plan,
                        onClick = { planId -> navController.navigate("planDetailScreen/${planId}")  }
                    )
                }
            }
        }
    }
}


@Composable
fun PlanItem(
    plan: Plans,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clickable { onClick(plan.planId) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = plan.planName,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        Text(
            text = plan.notificationTime,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}