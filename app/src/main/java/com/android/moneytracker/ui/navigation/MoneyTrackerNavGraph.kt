package com.android.moneytracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.moneytracker.ui.expenses.ExpenseDestination
import com.android.moneytracker.ui.expenses.ExpenseScreen


@Composable
fun MoneyTrackerNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController, startDestination = ExpenseDestination.route
    ) {
        composable(route = ExpenseDestination.route) {
            ExpenseScreen()
        }
    }
}