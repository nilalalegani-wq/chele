package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GuideScreen
import com.example.ui.screens.RecitationScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ChelehViewModel
import com.example.ui.viewmodel.ChelehViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val app = application as ChelehApplication
        val repository = app.repository

        setContent {
            MyApplicationTheme {
                val viewModel: ChelehViewModel = viewModel(
                    factory = ChelehViewModelFactory(repository)
                )
                
                val navController = rememberNavController()
                val days by viewModel.allDays.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "dashboard"
                ) {
                    composable("dashboard") {
                        DashboardScreen(
                            days = days,
                            onDayClick = { dayNumber ->
                                viewModel.selectDay(dayNumber)
                                navController.navigate("recitation/$dayNumber")
                            },
                            onGuideClick = {
                                navController.navigate("guide")
                            },
                            onResetClick = {
                                viewModel.resetAllProgress()
                            }
                        )
                    }
                    
                    composable("guide") {
                        GuideScreen(
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    
                    composable(
                        route = "recitation/{dayNumber}",
                        arguments = listOf(navArgument("dayNumber") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val dayNumber = backStackEntry.arguments?.getInt("dayNumber") ?: 1
                        val activeDay by viewModel.activeDay.collectAsState()
                        
                        LaunchedEffect(dayNumber) {
                            viewModel.selectDay(dayNumber)
                        }

                        activeDay?.let { day ->
                            RecitationScreen(
                                day = day,
                                onBackClick = {
                                    viewModel.clearSelectedDay()
                                    navController.popBackStack()
                                },
                                onSaveDay = { updatedDay ->
                                    viewModel.updateDay(updatedDay)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
