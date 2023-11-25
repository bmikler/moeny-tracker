package com.android.moneytracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.android.moneytracker.ui.expenses.ExpenseDestination
import com.android.moneytracker.ui.expenses.ExpenseEntryDestination
import com.android.moneytracker.ui.expenses.ExpenseEntryScreen
import com.android.moneytracker.ui.expenses.ExpenseScreen


@Composable
fun MoneyTrackerNavHost(
    navController: NavHostController,
) {

    NavHost(
        navController = navController, startDestination = ExpenseDestination.route
    ) {
        composable(route = ExpenseDestination.route) {
            ExpenseScreen(
                reloadScreen = { navController.navigate(ExpenseDestination.route) },
                navigateToAddExpanse = { navController.navigate("${ExpenseEntryDestination.route}/${it}") }
            )
        }
        composable(
            route = ExpenseEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(ExpenseEntryDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ExpenseEntryScreen(
                navigateBack = { navController.navigate(ExpenseDestination.route) }
            )
        }
    }
}