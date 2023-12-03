package com.android.moneytracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.android.moneytracker.model.Category
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

    val categoryKey = "category"

    NavHost(
        navController = navController, startDestination = ExpenseDestination.route
    ) {
        composable(route = ExpenseDestination.route) {
            ExpenseScreen(
                navigateToAddExpanse = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(categoryKey, it)
                    navController.navigate(ExpenseEntryDestination.route)
                },
                navigateToAddCategory = { navController.navigate(CategoryEntryDestination.route) }
            )
        }
        composable(
            route = ExpenseEntryDestination.route
        ) {
            val category = navController.previousBackStackEntry?.savedStateHandle?.get<Category>(categoryKey) ?: Category()
            ExpenseEntryScreen(
                category = category,
                navigateBack = { navController.navigate(ExpenseDestination.route) }
            )
        }
        composable(
            route = CategoryEntryDestination.route
        ) {
            CategoryEntryScreen(navigateBack = { navController.navigate(ExpenseDestination.route) })
        }
    }
}