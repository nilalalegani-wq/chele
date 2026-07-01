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
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ChelehViewModel
import com.example.ui.viewmodel.ChelehViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val app = application as ChelehApplication
        val repository = app.repository
        val settingsManager = app.settingsManager

        setContent {
            val viewModel: ChelehViewModel = viewModel(
                factory = ChelehViewModelFactory(repository, settingsManager)
            )
            val themeMode by viewModel.themeMode.collectAsState()
            val colorTheme by viewModel.colorTheme.collectAsState()

            MyApplicationTheme(themeMode = themeMode, colorTheme = colorTheme) {
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
                            onSettingsClick = {
                                navController.navigate("settings")
                            }
                        )
                    }

                    composable("settings") {
                        SettingsScreen(
                            viewModel = viewModel,
                            onBackClick = {
                                navController.popBackStack()
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
                        val vibrationEnabled by viewModel.vibrationEnabled.collectAsState()
                        
                        LaunchedEffect(dayNumber) {
                            viewModel.selectDay(dayNumber)
                        }

                        activeDay?.let { day ->
                            RecitationScreen(
                                day = day,
                                vibrationEnabled = vibrationEnabled,
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
