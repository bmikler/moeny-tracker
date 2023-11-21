package com.android.moneytracker.infrastructure

import android.content.Context
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.data.repository.InMemoryExpenseRepository

interface AppContainer {
    val expenseRepository: ExpenseRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val expenseRepository: ExpenseRepository by lazy {
        InMemoryExpenseRepository()
    }
}