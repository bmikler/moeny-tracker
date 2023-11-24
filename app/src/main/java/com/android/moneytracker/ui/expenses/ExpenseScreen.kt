package com.android.moneytracker.ui.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.moneytracker.R
import com.android.moneytracker.infrastructure.AppViewModelProvider
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.CostType
import com.android.moneytracker.model.Expense
import com.android.moneytracker.ui.MoneyTrackerTopAppBar
import com.android.moneytracker.ui.navigation.NavigationDestination
import java.math.BigDecimal


object ExpenseDestination : NavigationDestination {
    override val route: String = "expenses"
    override val titleRes: Int = R.string.title_expense
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel = viewModel(factory = AppViewModelProvider.Factory),
    reloadScreen: () -> Unit,
    navigateToAddExpanse: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.expenseUiState.collectAsState()

    Scaffold(
        topBar = {
            MoneyTrackerTopAppBar(
                title = stringResource(id = ExpenseDestination.titleRes),
                canNavigateBack = false,
                navigateBack = { }
            )
        }) { innerPadding ->
        ExpenseBody(
            date = "${uiState.date.monthValue} - ${uiState.date.year}",
            nextDate = {
                viewModel.nextDate()
                reloadScreen()
            },
            previousDate = {
                viewModel.previousDate()
                reloadScreen()
            },
            expensesWithCategories = uiState.expensesByCategory,
            addExpense = navigateToAddExpanse,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}


@Composable
fun ExpenseBody(
    date: String,
    nextDate: () -> Unit,
    previousDate: () -> Unit,
    expensesWithCategories: Map<Category, List<Expense>>,
    addExpense: (Int) -> Unit,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
    ) {

        ExpenseNavigation(nextDate, previousDate, date)

        expensesWithCategories.forEach {
            ExpandableCategory(
                category = it.key,
                alreadySpent = BigDecimal.ONE,
                leftToSpend = BigDecimal.ONE,
                expenses = it.value,
                addExpense = addExpense
            )
        }
    }
}


@Composable
fun ExpandableCategory(
    category: Category,
    alreadySpent: BigDecimal,
    leftToSpend: BigDecimal,
    expenses: List<Expense>,
    addExpense: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedState by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    expandedState = !expandedState
                }
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            CategoryHeader(category.name, alreadySpent, leftToSpend)
            IconButton(
                onClick = { addExpense(category.id) },
                modifier = Modifier.background(Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.btn_add)
                )
            }

        }

        if (expandedState && expenses.isNotEmpty()) {
            ExpenseList(
                expenses = expenses
            )
        }
    }

}

@Composable
private fun CategoryHeader(
    categoryName: String,
    alreadySpent: BigDecimal,
    leftToSpend: BigDecimal
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {

        Row {
            Text(
                text = categoryName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(6.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Already spent $alreadySpent")
            Text(text = "Left to spend $leftToSpend")
        }
    }

}


@Composable
private fun ExpenseList(
    expenses: List<Expense>
) {

    LazyColumn {
        items(expenses) { item ->
            ExpenseItem(item.description, item.value)
        }
    }
}

@Composable
private fun ExpenseItem(
    description: String,
    value: BigDecimal
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(text = description)
        Text(text = "$value PLN")
    }
}


@Composable
private fun ExpenseNavigation(
    navigateNext: () -> Unit,
    navigatePrevious: () -> Unit,
    date: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        IconButton(onClick = navigatePrevious) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_back)
            )
        }

        Text(text = date)

        IconButton(
            onClick = navigateNext,
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = stringResource(R.string.btn_back),
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun ExpandableCategoryPreview() {
//    ExpandableCategory(
//        category = Category(1, "test", CostType.IRREGULAR),
//        alreadySpent = BigDecimal.valueOf(25.55),
//        leftToSpend = BigDecimal.valueOf(25.55),
//        expenses = listOf()
//    )
//}


@Preview(showBackground = true)
@Composable
private fun ExpenseBodyPreview() {
    ExpenseBody(
        "10 - 2022",
        {},
        {},
        mapOf(Category(1, "test", BigDecimal.TEN, CostType.CONSTANT) to listOf()),
        {})
}

//@Preview(showBackground = true)
//@Composable
//private fun ExpanseItemPreview() {
//    ExpenseItem(description = "test", value = BigDecimal.valueOf(2023.55))
//}