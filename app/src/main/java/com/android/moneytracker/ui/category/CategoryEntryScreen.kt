package com.android.moneytracker.ui.category

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.toSize
import com.android.moneytracker.R
import com.android.moneytracker.ui.MoneyTrackerTopAppBar
import com.android.moneytracker.ui.navigation.NavigationDestination
import java.util.Currency

import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.moneytracker.infrastructure.AppViewModelProvider
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.ExpenseType
import java.util.Locale

object CategoryEntryDestination : NavigationDestination {
    override val route: String = "category_entry"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEntryScreen(
    viewModel: CategoryEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    category: Category? = null,
) {

    Log.d("TEST1", "category: $category ${category != null}")

    category?.let {
        Log.d("TEST2", "category: $it ${it != null}")
        viewModel.updateUiState(it.toDetails())
    }



    Scaffold(
        topBar = {
            MoneyTrackerTopAppBar(
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack
            )
        }
    )

    { innerPadding ->
        CategoryEntryBody(
            onSave = {
                viewModel.saveCategory()
                navigateBack()
            },
            isDeleteEnabled = category != null,
            onDelete = {},
            categoryEntryUiState = viewModel.entryUiState,
            onEntryValueChange = viewModel::updateUiState,
            toggleDropdown = viewModel::toggleDropdown,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryEntryBody(
    categoryEntryUiState: CategoryEntryUiState,
    onEntryValueChange: (CategoryEntryDetails) -> Unit,
    toggleDropdown: () -> Unit,
    onSave: () -> Unit = {},
    isDeleteEnabled: Boolean,
    onDelete: (Category) -> Unit,
    modifier: Modifier = Modifier
) {

    val entryDetails = categoryEntryUiState.categoryEntryDetails


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            OutlinedTextField(
                label = { Text(stringResource(id = R.string.label_category_name)) },
                value = entryDetails.name,
                onValueChange = {
                    onEntryValueChange(entryDetails.copy(name = it))
                },
                enabled = true,
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
            )

            OutlinedTextField(
                label = { Text(stringResource(id = R.string.label_spending_limit)) },
                value = entryDetails.spendingLimit,
                onValueChange = {
                    onEntryValueChange(entryDetails.copy(spendingLimit = it))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                trailingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
                singleLine = true,
                enabled = true,
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
            ) 

            ExpenseTypeDropdown(
                entryDetails = entryDetails,
                expenseTypes = categoryEntryUiState.expenseTypes,
                isExpanded = categoryEntryUiState.isTypeMenuExpanded,
                onEntryValueChange = onEntryValueChange,
                toggleDropdown = toggleDropdown,
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
            )


        }

        Spacer(modifier = Modifier.height(128.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onSave,
                enabled = categoryEntryUiState.isInputValid,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(text = stringResource(R.string.btn_save))
            }

            if (isDeleteEnabled) {
                Button(
                    onClick = {},
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(text = stringResource(R.string.btn_remove))
                }
            }


        }


    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTypeDropdown(
    entryDetails: CategoryEntryDetails,
    expenseTypes: List<ExpenseType>,
    onEntryValueChange: (CategoryEntryDetails) -> Unit,
    toggleDropdown: () -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown


    OutlinedTextField(
        value = entryDetails.type.name,
        onValueChange = { },
        readOnly = true,
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            },
        label = { Text(stringResource(id = R.string.label_expense_type)) },
        trailingIcon = {
            Icon(icon, stringResource(id = R.string.content_description_toggle_dropdown),
                Modifier.clickable { toggleDropdown() })
        }
    )

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { toggleDropdown() },
        modifier = Modifier
            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
    ) {
        expenseTypes.forEach { type ->
            DropdownMenuItem(
                text = { Text(text = type.name) },
                onClick = {
                    toggleDropdown()
                    onEntryValueChange(entryDetails.copy(type = type))
                })
        }
    }

}

