package com.android.moneytracker.data.repository

import com.android.moneytracker.data.CategoryTotal
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import com.android.moneytracker.model.ExpenseType
import java.time.LocalDate


interface ExpenseRepository {
    fun getCategories(): List<Category>
    suspend fun saveCategory(category: Category)
    suspend fun removeCategory(category: Category)
    suspend fun saveExpense(expense: Expense)
    suspend fun removeExpense(expense: Expense)
    fun getExpensesForDates(dateRange: Pair<LocalDate, LocalDate>): List<Expense>
    fun getAnnualExpensesSummedByCategory(dateRange: Pair<LocalDate, LocalDate>): List<CategoryTotal>
}
