package com.android.moneytracker.ui.expense


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.android.moneytracker.R
import com.android.moneytracker.ui.MoneyTrackerTopAppBar
import com.android.moneytracker.ui.navigation.NavigationDestination

import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.moneytracker.infrastructure.AppViewModelProvider
import com.android.moneytracker.model.Category
import com.android.moneytracker.ui.category.toDetails
import java.util.Currency
import java.util.Locale


object ExpenseEntryDestination : NavigationDestination {
    override val route: String = "expenses_entry"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(
    viewModel: ExpenseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    category: Category,
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
) {

    Scaffold(
        topBar = {
            MoneyTrackerTopAppBar(
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack
            )
        }
    )

    { innerPadding ->
        ExpenseEntryBody(
            category,
            viewModel.expenseEntryUiState,
            viewModel::updateUiState,
            onSave = {
                viewModel.saveEntry()
                navigateBack()
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseEntryBody(
    category: Category,
    expenseEntryUiState: ExpenseEntryUiState,
    onEntryValueChange: (ExpenseEntryDetails) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val entryDetails = expenseEntryUiState.expenseEntryDetails

        Text(text = category.name)

        OutlinedTextField(
            value = entryDetails.description,
            onValueChange = { onEntryValueChange(entryDetails.copy(description = it, categoryId = category.id)) },
            label = { Text(stringResource(id = R.string.label_expense_description)) },
            enabled = true
        )

        OutlinedTextField(
            value = entryDetails.value,
            onValueChange = {
                onEntryValueChange(entryDetails.copy(
                    value = it,
                    categoryId = category.id
                ))
            },
            label = { Text(stringResource(id = R.string.label_expense_value)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            trailingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            singleLine = true,
            enabled = true,
        )


        Button(
            onClick = onSave,
            enabled = expenseEntryUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
        ) {
            Text(text = stringResource(R.string.btn_save))
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun ExpenseEntryScreenPreview() {
//    ExpenseEntryScreen()
//}

