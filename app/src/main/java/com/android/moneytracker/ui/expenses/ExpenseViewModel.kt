package com.android.moneytracker.ui.expenses

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.CostType
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.math.log

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.saveCategory(Category(1, "test", BigDecimal.ZERO, CostType.CONSTANT))
            expenseRepository.saveCategory(Category(2, "test2", BigDecimal.ZERO, CostType.CONSTANT))
        }

    }

    val expenseUiState: StateFlow<ExpenseUiState> = expenseRepository.getCategoriesAndExpenses()
        .map { ExpenseUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ExpenseUiState()
        ).also { Log.d("abc", it.toString()) }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ExpenseUiState(
    val expensesByCategory: Map<Category, List<Expense>> = mapOf()
)
