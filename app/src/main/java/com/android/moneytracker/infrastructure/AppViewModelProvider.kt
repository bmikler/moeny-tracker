package com.android.moneytracker.infrastructure

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.moneytracker.MoneyTrackerApplication
import com.android.moneytracker.ui.expenses.ExpenseEntryViewModel
import com.android.moneytracker.ui.expenses.ExpenseViewModel
import com.android.moneytracker.ui.expenses.SharedDateViewModel

object AppViewModelProvider {

    private val sharedDateViewModel = SharedDateViewModel()

    var Factory = viewModelFactory {
        initializer {
            ExpenseEntryViewModel(
                this.createSavedStateHandle(),
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
    }
}

fun CreationExtras.moneyTrackerApplication(): MoneyTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MoneyTrackerApplication)