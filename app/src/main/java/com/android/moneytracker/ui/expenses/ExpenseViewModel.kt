package com.android.moneytracker.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val sharedDateViewModel: SharedDateViewModel
) : ViewModel() {

    private val categories = expenseRepository.getCategories()
    private val expenses = expenseRepository.getExpensesForDate(getDateRange())

    val expenseUiState: StateFlow<ExpenseUiState> =
        combine(categories, expenses) { categoriesList, expensesList ->
            categoriesList.associateWith { category ->
                expensesList.filter { expense -> expense.categoryId == category.id }
            }
        }
            .map { ExpenseUiState(date = sharedDateViewModel.date, expensesByCategory = it) }
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


}

data class ExpenseUiState(
    val date: LocalDate = LocalDate.now(),
    val expensesByCategory: Map<Category, List<Expense>> = mapOf()
)

