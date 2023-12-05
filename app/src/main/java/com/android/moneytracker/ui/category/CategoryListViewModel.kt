package com.android.moneytracker.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CategoryListViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    val entities: StateFlow<CategoryListUiState> = expenseRepository.getCategories()
        .map { CategoryListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed((TIMEOUT_MILLIS)),
            initialValue = CategoryListUiState()
        )

//    fun removeEntity(entity: UiEntity) {
//        viewModelScope.launch(Dispatchers.IO){
//            Log.d("Removing entity", "$entity")
//            groupRepository.deleteGroup(entity as Group)
//        }
//    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class CategoryListUiState (
    val categories: List<Category> = listOf()
)