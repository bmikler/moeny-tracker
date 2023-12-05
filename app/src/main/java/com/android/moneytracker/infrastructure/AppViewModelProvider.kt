package com.android.moneytracker.infrastructure

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.moneytracker.MoneyTrackerApplication
import com.android.moneytracker.ui.category.CategoryEntryViewModel
import com.android.moneytracker.ui.category.CategoryListViewModel
import com.android.moneytracker.ui.expense.ExpenseEntryViewModel
import com.android.moneytracker.ui.expense.ExpenseViewModel
import com.android.moneytracker.ui.expense.SharedDateViewModel

object AppViewModelProvider {

    private val sharedDateViewModel = SharedDateViewModel()

    var Factory = viewModelFactory {
        initializer {
            ExpenseEntryViewModel(
                moneyTrackerApplication().container.expenseRepository,
                sharedDateViewModel
            )
        }
        initializer {
            ExpenseViewModel(
                moneyTrackerApplication().container.expenseRepository,
                sharedDateViewModel
            )
        }
        initializer {
            CategoryListViewModel(
                moneyTrackerApplication().container.expenseRepository
            )
        }
        initializer {
            CategoryEntryViewModel(
                moneyTrackerApplication().container.expenseRepository
            )
        }
    }
}

fun CreationExtras.moneyTrackerApplication(): MoneyTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MoneyTrackerApplication)