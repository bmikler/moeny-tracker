package com.android.moneytracker.ui.expenses

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.moneytracker.R
import com.android.moneytracker.ui.MoneyTrackerTopAppBar
import com.android.moneytracker.ui.navigation.NavigationDestination


object ExpenseDestination : NavigationDestination {
    override val route: String = "spending"
    override val titleRes: Int = R.string.title_expense

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    modifier: Modifier = Modifier,
) {

    Scaffold(
        topBar = {
            MoneyTrackerTopAppBar(
                title = stringResource(id = ExpenseDestination.titleRes),
                canNavigateBack = true,
                navigateUp = {  }
            )
        }) { innerPadding ->
            ExpenseBody(modifier = modifier.padding(innerPadding))
    }

}

@Composable
fun ExpenseBody(modifier: Modifier = Modifier) {
    Text(text = "hello")
}