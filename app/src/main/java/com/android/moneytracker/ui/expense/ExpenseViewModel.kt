package com.android.moneytracker.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.CategoryTotal
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.ExpenseType
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


    private val _uiState = MutableStateFlow(ExpenseUiState(sharedDateViewModel.date.toString(), mapOf()))
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init {
        reloadUiState()
    }

    fun nextDate() {
        sharedDateViewModel.nextMonth()
        reloadUiState()
    }

    fun previousDate() {
        sharedDateViewModel.previousMonth()
        reloadUiState()
    }

    private fun reloadUiState() {
        viewModelScope.launch(Dispatchers.IO) {
            val expensesByCategoryAnnual = expenseRepository.getAnnualExpensesSummedByCategory(sharedDateViewModel.date.getYearRange())
            val categoriesWithExpenses = expenseRepository.getCategoriesWithExpenses(sharedDateViewModel.date.getMonthRange())

            val categoriesUiWithExpenses: Map<CategoryUi, List<Expense>> =
                categoriesWithExpenses.mapKeys { (category, expenses) ->

                    val alreadySpent = when (category.type) {
                        ExpenseType.MONTHLY -> expenses.sumOf { it.value }
                        ExpenseType.ANNUAL -> expensesByCategoryAnnual.getTotalForCategory(category.id)
                    }

                    CategoryUi(
                        id = category.id,
                        name = category.name,
                        alreadySpent = alreadySpent,
                        leftToSpent = category.spendingLimit.minus(alreadySpent),
                        type = category.type
                    )
                }

            _uiState.update {
                ExpenseUiState(sharedDateViewModel.date.getString(), categoriesUiWithExpenses)
            }
        }
    }
    private fun List<CategoryTotal>.getTotalForCategory(id: Int) = (firstOrNull { it.id == id }?.total) ?: BigDecimal.ZERO
    private fun LocalDate.getString() = "$month - $year"
    private fun LocalDate.getMonthRange() = LocalDate.of(year, month, 1) to LocalDate.of(year, month, lengthOfMonth())
    private fun LocalDate.getYearRange() = LocalDate.of(year, 1, 1) to LocalDate.of(year, 12, lengthOfMonth())
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
    val type: ExpenseType
)


fun CategoryUi.toCategory() = Category(id, name, alreadySpent + leftToSpent, type)



