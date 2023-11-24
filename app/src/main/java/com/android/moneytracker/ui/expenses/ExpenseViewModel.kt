package com.android.moneytracker.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.CostType
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val sharedDateViewModel: SharedDateViewModel
) : ViewModel() {

    private val categories = expenseRepository.getCategories()
    private val expenses = expenseRepository.getExpensesForDate(getDateRange())

    val expenseUiState: StateFlow<ExpenseUiState> =
        combine(categories, expenses) { categoriesList, expensesList ->
            categoriesList.associateWith { category -> expensesList.filter { expense -> expense.categoryId == category.id } }
        }
            .map { mapToExpenseUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExpenseUiState()
            )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun nextDate() {
        sharedDateViewModel.nextMonth()
    }

    fun previousDate() {
        sharedDateViewModel.previousMonth()
    }

    private fun getDateRange(): Pair<LocalDate, LocalDate> = with(sharedDateViewModel.date) {
        LocalDate.of(year, month, 1) to LocalDate.of(year, month, lengthOfMonth())
    }


    private fun mapToExpenseUiState(expensesWithCategories: Map<Category, List<Expense>>): ExpenseUiState {
        return ExpenseUiState(date = sharedDateViewModel.date,
            expensesByCategory = expensesWithCategories.mapKeys { (category, expenses) ->
                val alreadySpent = expenses.sumOf { it.value }
                CategoryUi(
                    category.id,
                    category.name,
                    alreadySpent,
                    category.spendingLimit.minus(alreadySpent),
                    category.type
                )
            })
    }
}


data class ExpenseUiState(
    val date: LocalDate = LocalDate.now(),
    val expensesByCategory: Map<CategoryUi, List<Expense>> = mapOf()
)

data class CategoryUi(
    val id: Int,
    val name: String,
    val alreadySpent: BigDecimal,
    val leftToSpent: BigDecimal,
    val type: CostType
)