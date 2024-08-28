package com.example.fitnessapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitnessapp.view.ui.theme.FitnessAppTheme
import com.example.fitnessapp.viewmodel.PlanListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "mainScreen"){

                    composable("mainScreen"){
                        PlanListScreen(navController = navController)
                    }
                    composable("createPlanScreen"){
                        CreatePlanScreen(navController = navController)
                    }
                    composable("planDetailScreen/{planId}", arguments = listOf(
                            navArgument("planId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val planId = backStackEntry.arguments?.getInt("planId")
                        if (planId != null) {
                            PlanDetailScreen(navController = navController, id = planId)
                        }
                    }
                }
            }
        }
    }
}
