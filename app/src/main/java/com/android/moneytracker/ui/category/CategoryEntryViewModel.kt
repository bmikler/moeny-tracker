package com.android.moneytracker.ui.category

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.ExpenseType
import com.android.moneytracker.ui.expense.toBigDecimalSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CategoryEntryViewModel(private var expenseRepository: ExpenseRepository) : ViewModel() {

    var entryUiState by mutableStateOf(CategoryEntryUiState())
        private set


    fun updateUiState(categoryEntryDetails: CategoryEntryDetails) {
        Log.d("test123", "updateUiState: $categoryEntryDetails")
        entryUiState = CategoryEntryUiState(categoryEntryDetails = categoryEntryDetails, isValid = validateInput(categoryEntryDetails))
    }

    fun updateType(type: ExpenseType) {
        val categoryEntryDetails = with(entryUiState.categoryEntryDetails) {
            CategoryEntryDetails(name, spendingLimit, type)
        }

        entryUiState = CategoryEntryUiState(categoryEntryDetails = categoryEntryDetails, isValid = validateInput(categoryEntryDetails))
    }

    fun saveCategory() {
        if (validateInput()) {

            val category = with(entryUiState.categoryEntryDetails) {
                Category(name = name, spendingLimit = spendingLimit.toBigDecimalSafe(), type = type)
            }


            viewModelScope.launch(Dispatchers.IO) {
                Log.d("Saving category: ", category.toString())
                expenseRepository.saveCategory(category)
            }

        }
    }

    private fun validateInput(uiState: CategoryEntryDetails = entryUiState.categoryEntryDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && spendingLimit.isNotBlank() && spendingLimit.toBigDecimalSafe() > BigDecimal.ZERO
        }
    }

}

data class CategoryEntryUiState (
    val isValid: Boolean = false,
    val expenseTypes: List<ExpenseType> = ExpenseType.values().toList(),
    val categoryEntryDetails: CategoryEntryDetails = CategoryEntryDetails()
)

data class CategoryEntryDetails(
    val name: String = "",
    val spendingLimit: String = "0",
    val type: ExpenseType = ExpenseType.MONTHLY
)