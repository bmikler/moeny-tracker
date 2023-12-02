package com.android.moneytracker.ui.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.RowScopeInstance.weight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.android.moneytracker.R
import com.android.moneytracker.ui.MoneyTrackerTopAppBar
import com.android.moneytracker.ui.navigation.NavigationDestination
import java.util.Currency

import androidx.compose.material3.Button
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.moneytracker.infrastructure.AppViewModelProvider
import com.android.moneytracker.model.ExpenseType
import com.android.moneytracker.ui.expense.ExpenseEntryDetails
import com.android.moneytracker.ui.expense.ExpenseEntryUiState
import java.util.Locale

object CategoryEntryDestination : NavigationDestination {
    override val route: String = "category_entry"
    override val titleRes: Int = R.string.title_category_entry
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEntryScreen(
    viewModel: CategoryEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
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
        CategoryEntryBody(
            onSave = {
                viewModel.saveCategory()
                navigateBack()
            },
            categoryEntryUiState = viewModel.entryUiState,
            onEntryValueChange = viewModel::updateUiState,
            onTypeChange = viewModel::updateType,
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
    onTypeChange: (ExpenseType) -> Unit,
    onSave: () -> Unit = {},
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
//                label = { Text("ExpenseType") },
                value = entryDetails.name,
                onValueChange = {onEntryValueChange(entryDetails.copy(name = it))
                },
                enabled = true,
            )

            OutlinedTextField(
//                label = { Text("ExpenseType") },
                value = entryDetails.spendingLimit,
                onValueChange = {
                onEntryValueChange(entryDetails.copy(spendingLimit = it))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                trailingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
                singleLine = true,
                enabled = true,
            )

            ExpenseTypeDropdown(
                selectedType = entryDetails.type,
                expenseTypes = categoryEntryUiState.expenseTypes,
                onTypeChange = onTypeChange
            )
            

        }

        Spacer(modifier = Modifier.height(128.dp))
        Column {
            Button(
                onClick = onSave,
                enabled = categoryEntryUiState.isValid,
//            entryUiState.isEntryValid
                shape = MaterialTheme.shapes.small,
            ) {
                Text(text = stringResource(R.string.save_action))
            }
        }


    }




}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTypeDropdown(
    selectedType: ExpenseType,
    expenseTypes: List<ExpenseType>,
    onTypeChange: (ExpenseType) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown



    OutlinedTextField(
        value = selectedType.name,
        onValueChange = { onTypeChange(ExpenseType.valueOf(it)) },
        readOnly = true,
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            },
//        label = { Text("ExpenseType") },
        trailingIcon = {
            Icon(icon, "contentDescription",
                Modifier.clickable { isExpanded = !isExpanded })
        }
    )

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false },
        modifier = Modifier
            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
    ) {
        expenseTypes.forEach { type ->
            DropdownMenuItem(
                text = { Text(text = type.name) },
                onClick = {
                    onTypeChange(type)
                    isExpanded = false
                })
        }
    }

}

