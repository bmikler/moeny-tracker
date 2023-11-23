package com.android.moneytracker.infrastructure

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.moneytracker.MoneyTrackerApplication
import com.android.moneytracker.ui.expenses.ExpenseEntryViewModel
import com.android.moneytracker.ui.expenses.ExpenseViewModel

object AppViewModelProvider {
    var Factory = viewModelFactory {
        initializer {
            ExpenseEntryViewModel(
                this.createSavedStateHandle(),
                moneyTrackerApplication().container.expenseRepository
            )
        }
        initializer {
            ExpenseViewModel(
                moneyTrackerApplication().container.expenseRepository
            )
        }
    }
}

fun CreationExtras.moneyTrackerApplication(): MoneyTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MoneyTrackerApplication)