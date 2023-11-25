package com.android.moneytracker.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.CategoryTotal
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.CostType
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate


class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val sharedDateViewModel: SharedDateViewModel
) : ViewModel() {

    private lateinit var categories: List<Category>
    private lateinit var expenses: List<Expense>
    private lateinit var annualExpensesSummed: List<CategoryTotal>

    private val _uiState = MutableStateFlow(ExpenseUiState(sharedDateViewModel.date.toString(), mapOf()))
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init {
        updateUiState()
    }

    fun nextDate() {
        sharedDateViewModel.nextMonth()
        updateUiState()
    }

    fun previousDate() {
        sharedDateViewModel.previousMonth()
        updateUiState()
    }

    private fun updateUiState() {
        viewModelScope.launch(Dispatchers.IO) {
            categories = expenseRepository.getCategories()
            expenses = expenseRepository.getExpensesForDates(sharedDateViewModel.date.getMonthRange())
            annualExpensesSummed = expenseRepository.getAnnualExpensesSummedByCategory(sharedDateViewModel.date.getYearRange())

            val expensesByCategory = categories.associateWith { category -> expenses.filter { expense -> expense.categoryId == category.id } }

            _uiState.update {
                ExpenseUiState(sharedDateViewModel.date.getString(), mapToUiCategories(expensesByCategory))
            }
        }
    }

    private fun mapToUiCategories(expensesWithCategories: Map<Category, List<Expense>>): Map<CategoryUi, List<Expense>> {
        return expensesWithCategories.mapKeys { (category, expenses) ->

                val spentInCurrentMonth = expenses.sumOf { it.value }

                val leftToSpent = when (category.type) {
                    CostType.MONTHLY -> category.spendingLimit.minus(spentInCurrentMonth)
                    CostType.ANNUAL -> category.spendingLimit.minus((annualExpensesSummed.firstOrNull { it.id == category.id }?.total) ?: BigDecimal.ZERO)
                }

                CategoryUi(
                    id = category.id,
                    name = category.name,
                    alreadySpent = spentInCurrentMonth,
                    leftToSpent = leftToSpent,
                    type = category.type
                )
            }
    }


    private fun LocalDate.getString() = "$month - $year"
    private fun LocalDate.getMonthRange() =
        LocalDate.of(year, month, 1) to LocalDate.of(year, month, lengthOfMonth())

    private fun LocalDate.getYearRange() =
        LocalDate.of(year, 1, 1) to LocalDate.of(year, 12, lengthOfMonth())
}


data class ExpenseUiState(
    val date: String,
    val expensesByCategory: Map<CategoryUi, List<Expense>>
)


data class CategoryUi(
    val id: Int,
    val name: String,
    val alreadySpent: BigDecimal,
    val leftToSpent: BigDecimal,
    val type: CostType
)

