package com.redifor.diarysof.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.redifor.diarysof.ui.screens.splash.SplashScreen
import com.redifor.diarysof.ui.screens.onboarding.OnboardingScreen
import com.redifor.diarysof.ui.screens.today.TodayScreen
import com.redifor.diarysof.ui.screens.addnote.AddNoteScreen
import com.redifor.diarysof.ui.screens.calendar.CalendarScreen
import com.redifor.diarysof.ui.screens.daydetails.DayDetailsScreen
import com.redifor.diarysof.ui.screens.statistics.StatisticsScreen
import com.redifor.diarysof.ui.screens.streaks.StreaksScreen
import com.redifor.diarysof.ui.screens.reminders.RemindersScreen
import com.redifor.diarysof.ui.screens.export.ExportScreen
import com.redifor.diarysof.ui.screens.settings.SettingsScreen
import com.redifor.diarysof.viewmodel.DiaryViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: DiaryViewModel,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            val onboardingCompleted by viewModel.onboardingCompleted.collectAsState(initial = false)
            
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToToday = {
                    navController.navigate(Screen.Today.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                isOnboardingCompleted = onboardingCompleted
            )
        }
        
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                viewModel = viewModel,
                onFinish = {
                    navController.navigate(Screen.Today.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Today.route) {
            TodayScreen(
                viewModel = viewModel,
                onNavigateToAddNote = { date ->
                    navController.navigate(Screen.AddNote.createRoute(date)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToCalendar = {
                    navController.navigate(Screen.Calendar.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToStreaks = {
                    navController.navigate(Screen.Streaks.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToReminders = {
                    navController.navigate(Screen.Reminders.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToExport = {
                    navController.navigate(Screen.Export.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(
            route = Screen.AddNote.route,
            arguments = listOf(navArgument("date") { type = NavType.LongType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getLong("date") ?: 0L
            AddNoteScreen(
                date = date,
                viewModel = viewModel,
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Screen.Today.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Calendar.route) {
            CalendarScreen(
                viewModel = viewModel,
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Screen.Today.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToDayDetails = { date ->
                    navController.navigate(Screen.DayDetails.createRoute(date)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(
            route = Screen.DayDetails.route,
            arguments = listOf(navArgument("date") { type = NavType.LongType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getLong("date") ?: 0L
            DayDetailsScreen(
                date = date,
                viewModel = viewModel,
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Screen.Today.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToEdit = { editDate ->
                    navController.navigate(Screen.AddNote.createRoute(editDate)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen(
                viewModel = viewModel,
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Screen.Today.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Streaks.route) {
            StreaksScreen(
                viewModel = viewModel,
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Screen.Today.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Reminders.route) {
            RemindersScreen(
                viewModel = viewModel,
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Screen.Today.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Export.route) {
            ExportScreen(
                viewModel = viewModel,
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Screen.Today.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Today.route) {
                            popUpTo(Screen.Today.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

