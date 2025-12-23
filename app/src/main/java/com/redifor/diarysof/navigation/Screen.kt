package com.redifor.diarysof.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Today : Screen("today")
    object AddNote : Screen("add_note/{date}") {
        fun createRoute(date: Long) = "add_note/$date"
    }
    object Calendar : Screen("calendar")
    object DayDetails : Screen("day_details/{date}") {
        fun createRoute(date: Long) = "day_details/$date"
    }
    object Statistics : Screen("statistics")
    object Streaks : Screen("streaks")
    object Reminders : Screen("reminders")
    object Export : Screen("export")
    object Settings : Screen("settings")
}

