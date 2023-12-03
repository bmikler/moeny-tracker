package com.android.moneytracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.android.moneytracker.ui.category.CategoryEntryDestination
import com.android.moneytracker.ui.category.CategoryEntryScreen
import com.android.moneytracker.ui.expense.ExpenseDestination
import com.android.moneytracker.ui.expense.ExpenseEntryDestination
import com.android.moneytracker.ui.expense.ExpenseEntryScreen
import com.android.moneytracker.ui.expense.ExpenseScreen


@Composable
fun MoneyTrackerNavHost(
    navController: NavHostController,
) {

    NavHost(
        navController = navController, startDestination = ExpenseDestination.route
    ) {
        composable(route = ExpenseDestination.route) {
            ExpenseScreen(
                navigateToAddExpanse = { navController.navigate("${ExpenseEntryDestination.route}/${it}") },
                navigateToAddCategory = { navController.navigate(CategoryEntryDestination.route) }
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
        composable(
            route = CategoryEntryDestination.route
        ) {
            CategoryEntryScreen(navigateBack = { navController.navigate(ExpenseDestination.route)})
        }
    }
}