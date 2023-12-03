package com.android.moneytracker.ui.expense

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseEntryViewModel(
    private val expanseRepository: ExpenseRepository,
    private val sharedDateViewModel: SharedDateViewModel
) : ViewModel() {

    var expenseEntryUiState by mutableStateOf(ExpenseEntryUiState(ExpenseEntryDetails()))
        private set

    fun updateUiState(expenseEntryDetails: ExpenseEntryDetails) {
        expenseEntryUiState = ExpenseEntryUiState(expenseEntryDetails = expenseEntryDetails, isEntryValid = validateInput(expenseEntryDetails))
    }

    fun saveEntry() {
        if (validateInput()) {
            val expense = expenseEntryUiState.expenseEntryDetails.toEntity(sharedDateViewModel.date)
            Log.d("Saving expense", expense.toString())

            viewModelScope.launch(Dispatchers.IO) {
                expanseRepository.saveExpense(expense)
            }

        }
    }

    private fun validateInput(uiState: ExpenseEntryDetails = expenseEntryUiState.expenseEntryDetails): Boolean {
        return with(uiState) {
            description.isNotBlank() && value.isNotBlank() && value.toBigDecimalSafe() > BigDecimal.ZERO
        }
    }
}

data class ExpenseEntryUiState(
    val expenseEntryDetails: ExpenseEntryDetails,
    val isEntryValid: Boolean = false
)

data class ExpenseEntryDetails(
    val description: String = "",
    val value: String = "",
    val categoryId: Int = -1
)


fun ExpenseEntryDetails.toEntity(date: LocalDate) = Expense(description = description, value = value.toBigDecimalSafe(), timestamp = date, categoryId = categoryId)

fun String.toBigDecimalSafe(): BigDecimal = this.replace(",", ".").toBigDecimalOrNull() ?: BigDecimal.ZERO