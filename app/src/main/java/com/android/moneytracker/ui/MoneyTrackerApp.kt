package com.android.moneytracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.android.moneytracker.ui.navigation.MoneyTrackerNavHost

@Composable
fun MoneyTrackerApp(
    navController: NavHostController = rememberNavController()
) {
    Column {
        MoneyTrackerNavHost(navController = navController)
    }
}