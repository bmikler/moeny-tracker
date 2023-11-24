package com.android.moneytracker.data.repository

import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime


interface ExpenseRepository {

    suspend fun saveCategory(category: Category)
    suspend fun saveExpense(expense: Expense)

    fun getCategories(): Flow<List<Category>>
    fun getExpensesForDate(dateRange: Pair<LocalDate, LocalDate>): Flow<List<Expense>>

//    fun getCategoriesAndExpenses() : Flow<Map<Category, List<Expense>>>
}
