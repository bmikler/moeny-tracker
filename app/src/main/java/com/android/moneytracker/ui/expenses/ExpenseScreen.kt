package com.android.moneytracker.ui.expenses

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.moneytracker.R
import com.android.moneytracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ExpenseDestination : NavigationDestination {
    override val route: String = "spending"
    override val titleRes: Int = R.string.title_expense

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen() {

    Scaffold(
        topBar = {
            EduTrackerTopAppBar(
                title = title,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }) { innerPadding ->
        ItemEntryBody(
            groupUiState = viewModel.groupUiState,
            onItemValueChange = { viewModel.updateUiState(it) },
            onSaveClick = {
                coroutineScope.launch { viewModel.saveGroup() }
                navigateBack()
            },
            isButtonEnabled = viewModel.isEntryValid(),
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }

}