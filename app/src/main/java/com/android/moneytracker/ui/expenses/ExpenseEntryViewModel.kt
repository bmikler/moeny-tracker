package com.android.moneytracker.ui.expenses

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Expense
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val expanseRepository: ExpenseRepository
) : ViewModel() {

    private val categoryId: Int = checkNotNull(savedStateHandle[ExpenseEntryDestination.itemIdArg])
    var entryUiState by mutableStateOf(EntryUiState(EntryDetails(categoryId = categoryId)))
        private set

    fun updateUiState(entryDetails: EntryDetails) {
        entryUiState =
            EntryUiState(entryDetails = entryDetails, isEntryValid = validateInput(entryDetails))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveEntry() {
        if (validateInput()) {
            val expense = entryUiState.entryDetails.toEntity()
            Log.d("Saving expense", expense.toString())
            expanseRepository.saveExpense(expense)
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

@RequiresApi(Build.VERSION_CODES.O)
fun EntryDetails.toEntity() =
    Expense(1, description, value.toBigDecimalSafe(), LocalDate.now(), categoryId)

fun String.toBigDecimalSafe(): BigDecimal =
    this.replace(",", ".").toBigDecimalOrNull() ?: BigDecimal.ZERO