package com.android.moneytracker.ui.expenses

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.moneytracker.R
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.CostType
import com.android.moneytracker.model.Expense
import com.android.moneytracker.ui.MoneyTrackerTopAppBar
import com.android.moneytracker.ui.navigation.NavigationDestination
import com.android.moneytracker.ui.theme.Shapes
import java.math.BigDecimal
import java.time.LocalDate


object ExpenseDestination : NavigationDestination {
    override val route: String = "spending"
    override val titleRes: Int = R.string.title_expense

}

@RequiresApi(Build.VERSION_CODES.O)
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
                navigateUp = { }
            )
        }) { innerPadding ->
        ExpenseBody(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()

        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseBody(modifier: Modifier = Modifier) {
    CategoriesList(
        categories = listOf(
            Category(1, "Food", CostType.CONSTANT),
            Category(2, "Gas", CostType.CONSTANT),
            Category(3, "Cloths", CostType.CONSTANT)
        ),
        modifier = modifier)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoriesList(
    categories: List<Category>,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        categories.forEach{ ExpandableCategory(category = it.name)}
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCategory(
    category: String,
    modifier: Modifier = Modifier
) {
    var expandedState by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    delayMillis = 10,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = Shapes.medium,
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row {
                Text(
                    text = category,
                    fontWeight = FontWeight.Bold
                )
            }

            if (expandedState) {
                ExpenseList(
                    expenses = listOf(
                        Expense(1, "McDonalds", BigDecimal.valueOf(25, 51), LocalDate.now(), 1),
                        Expense(2, "Restaurant", BigDecimal.valueOf(100, 51), LocalDate.now(), 1)
                    )
                )
            }
        }
    }
}


@Composable
fun ExpenseList(
    expenses: List<Expense>
) {

    LazyColumn {
        items(expenses) { item ->
            Row {
                Text(text = item.description)
            }
        }
    }
}


//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//
//        Text(text = categoryName, fontSize = 21.sp)
//
//        Row(
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Already spent $alreadySpent")
//            Text(text = "Left to spend $leftToSpent")
//        }
//
//    }


@Composable
fun ExpenseNavigation(
    navigateNext: () -> Unit = {},
    navigatePrevious: () -> Unit = {},
    date: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = navigatePrevious) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_back)
            )
        }

        Text(text = date)

        IconButton(onClick = navigateNext) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = stringResource(R.string.btn_back)
            )
        }
    }
}

@SuppressLint("NewApi")
@Preview(showBackground = true)
@Composable
fun ExpanseScreenPreview() {
    ExpenseScreen()
}

//@Preview(showBackground = true)
//@Composable
//fun ExpenseNavigationPreview() {
//    ExpenseNavigation(date = "10-10-2022")
//}