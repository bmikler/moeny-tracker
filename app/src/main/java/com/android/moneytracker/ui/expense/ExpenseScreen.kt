package com.android.moneytracker.ui.expense

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.moneytracker.R
import com.android.moneytracker.infrastructure.AppViewModelProvider
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.ExpenseType
import com.android.moneytracker.model.Expense
import com.android.moneytracker.ui.MoneyTrackerTopAppBar
import com.android.moneytracker.ui.navigation.NavigationDestination
import java.math.BigDecimal


object ExpenseDestination : NavigationDestination {
    override val route: String = "expenses"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToAddExpanse: (Category) -> Unit,
    navigateToAddCategory: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            MoneyTrackerTopAppBar(
                isActionEnable = true,
                onActionButton = navigateToAddCategory
            )
        }
    ) { innerPadding ->
        ExpenseBody(
            date = uiState.date,
            nextDate = { viewModel.nextDate() },
            previousDate = { viewModel.previousDate() },
            categoriesWithExpenses = uiState.expensesByCategory,
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
    categoriesWithExpenses: Map<CategoryUi, List<Expense>>,
    addExpense: (Category) -> Unit,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
    ) {

        ExpenseNavigation(nextDate, previousDate, date)

        LazyColumn {
            items(categoriesWithExpenses.entries.toList(), key = { it.key.id }) { entry ->
                ExpandableCategory(
                    categoryUi = entry.key,
                    expenses = entry.value,
                    addExpense = addExpense
                )
            }
        }
    }
}


@Composable
fun ExpandableCategory(
    categoryUi: CategoryUi,
    expenses: List<Expense>,
    addExpense: (Category) -> Unit,
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
            CategoryHeader(categoryUi)
            IconButton(
                onClick = { addExpense(categoryUi.toCategory()) },
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
    category: CategoryUi
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {

        Row {
            Text(
                text = category.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(6.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.label_already_spent_month))
                Text(text = "${category.alreadySpent} PLN")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                when (category.type) {
                    ExpenseType.ANNUAL -> Text(text = stringResource(R.string.label_left_to_spend_year))
                    ExpenseType.MONTHLY -> Text(text = stringResource(R.string.label_left_to_spend_month))
                }

                Text(text = "${category.leftToSpent} PLN")

            }
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


//@Preview(showBackground = true)
//@Composable
//private fun ExpenseBodyPreview() {
//    ExpenseBody(
//        "10 - 2022",
//        {},
//        {},
//        mapOf(Category(1, "test", BigDecimal.TEN, CostType.CONSTANT) to listOf()),
//        {})
//}

//@Preview(showBackground = true)
//@Composable
//private fun ExpanseItemPreview() {
//    ExpenseItem(description = "test", value = BigDecimal.valueOf(2023.55))
//}