package com.android.moneytracker.ui.expenses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.CategoryTotal
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.CostType
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val sharedDateViewModel: SharedDateViewModel
) : ViewModel() {

    private val date: LocalDate = sharedDateViewModel.date
    private lateinit var categories: List<Category>
    private lateinit var  expenses : List<Expense>
    private lateinit var annualExpensesSummed: List<CategoryTotal>
    var expenseUiState : ExpenseUiState = ExpenseUiState()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.saveCategory(Category(1, "miesieczna", BigDecimal(100), CostType.MONTHLY))
            expenseRepository.saveCategory(Category(2, "roczna", BigDecimal(2000), CostType.ANNUAL))

            categories = expenseRepository.getCategories()
            expenses = expenseRepository.getExpensesForDate(date.getMonthRange())
            annualExpensesSummed = expenseRepository.getAnnualExpensesSummedByCategory(date.getYearRange())
            expenseUiState = mapToExpenseUiState(categories.associateWith { category -> expenses.filter { expense -> expense.categoryId == category.id } })
        }

    }

    fun nextDate() {
        sharedDateViewModel.nextMonth()
    }

    fun previousDate() {
        sharedDateViewModel.previousMonth()
    }


    private fun mapToExpenseUiState(expensesWithCategories: Map<Category, List<Expense>>): ExpenseUiState {
        return ExpenseUiState(date = with(sharedDateViewModel.date) {"$month - $year"},
            expensesByCategory = expensesWithCategories.mapKeys { (category, expenses) ->

                Log.d("test", annualExpensesSummed.toString())
                Log.d("test2", "category id: ${category.id} - ${annualExpensesSummed.firstOrNull{ it.id == category.id}}")
                Log.d("test3", "${category.id} - ${annualExpensesSummed.firstOrNull{ it.id == category.id}?.total}")

                val spentInCurrentMonth = expenses.sumOf { it.value }

                val leftToSpent = when (category.type) {
                    CostType.MONTHLY -> category.spendingLimit.minus(spentInCurrentMonth)
                    CostType.ANNUAL -> category.spendingLimit.minus((annualExpensesSummed.firstOrNull{ it.id == category.id}?.total) ?: BigDecimal.ZERO)
                }

                CategoryUi(
                    category.id,
                    category.name,
                    spentInCurrentMonth,
                    leftToSpent,
                    category.type
                )
            })
    }




    private fun LocalDate.getMonthRange() = LocalDate.of(year, month, 1) to LocalDate.of(year, month, lengthOfMonth())
    private fun LocalDate.getYearRange() = LocalDate.of(year, 1, 1) to LocalDate.of(year, 12, lengthOfMonth())
}


data class ExpenseUiState(
    val date: String = "",
    val expensesByCategory: Map<CategoryUi, List<Expense>> = mapOf()
)



data class CategoryUi(
    val id: Int,
    val name: String,
    val alreadySpent: BigDecimal,
    val leftToSpent: BigDecimal,
    val type: CostType
)

