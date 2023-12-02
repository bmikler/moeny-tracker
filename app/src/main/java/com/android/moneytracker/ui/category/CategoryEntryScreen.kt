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
import com.android.moneytracker.model.ExpenseType
import java.util.Locale

object CategoryEntryDestination : NavigationDestination {
    override val route: String = "category_entry"
    override val titleRes: Int = R.string.title_category_entry
    const val itemIdArg = "itemId"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEntryScreen(
//    viewModel: ExpenseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
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
//                viewModel.saveEntry()
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
fun CategoryEntryBody(
    onSave: () -> Unit = {},
    modifier: Modifier = Modifier
) {

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
                value = "",
                onValueChange = { },
                enabled = true,
            )

            OutlinedTextField(
//                label = { Text("ExpenseType") },
                value = "entryDetails.value",
                onValueChange = {
//                onEntryValueChange(entryDetails.copy(value = it))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                trailingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
                singleLine = true,
                enabled = true,
            )



            ExpenseTypeDropdown(expenseTypes = listOf("test1", "test2"))
            

        }

        Spacer(modifier = Modifier.height(128.dp))
        Column {
            Button(
                onClick = onSave,
                enabled = true,
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
    expenseTypes: List<String>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }


    // Create a string value to store the selected city
    var selectedType by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown



    OutlinedTextField(
        value = selectedType,
        onValueChange = { selectedType = it },
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
        expenseTypes.forEach { label ->
            DropdownMenuItem(
                text = { Text(text = label) },
                onClick = {
                    selectedType = label
                    isExpanded = false
                })
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CategoryEntryScreen({})
}
