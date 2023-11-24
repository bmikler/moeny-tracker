package com.android.moneytracker.ui.expenses

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
    savedStateHandle: SavedStateHandle,
    private val expanseRepository: ExpenseRepository,
    private val sharedDateViewModel: SharedDateViewModel
) : ViewModel() {

    private val categoryId: Int = checkNotNull(savedStateHandle[ExpenseEntryDestination.itemIdArg])
    var entryUiState by mutableStateOf(EntryUiState(EntryDetails(categoryId = categoryId)))
        private set

    fun updateUiState(entryDetails: EntryDetails) {
        entryUiState =
            EntryUiState(entryDetails = entryDetails, isEntryValid = validateInput(entryDetails))
    }

    fun saveEntry() {
        if (validateInput()) {
            val expense = entryUiState.entryDetails.toEntity(sharedDateViewModel.date)
            Log.d("Saving expense", expense.toString())

            viewModelScope.launch(Dispatchers.IO) {
                expanseRepository.saveExpense(expense)
            }

        }
    }

    private fun validateInput(uiState: EntryDetails = entryUiState.entryDetails): Boolean {
        return with(uiState) {
            description.isNotBlank() && value.isNotBlank() && value.toBigDecimalSafe() > BigDecimal.ZERO
        }
    }
}

data class EntryUiState(
    val entryDetails: EntryDetails,
    val isEntryValid: Boolean = false
)

data class EntryDetails(
    val description: String = "",
    val value: String = "",
    val categoryId: Int
)


fun EntryDetails.toEntity(date: LocalDate) = Expense(description = description, value = value.toBigDecimalSafe(), timestamp = date, categoryId = categoryId)

fun String.toBigDecimalSafe(): BigDecimal =
    this.replace(",", ".").toBigDecimalOrNull() ?: BigDecimal.ZERO