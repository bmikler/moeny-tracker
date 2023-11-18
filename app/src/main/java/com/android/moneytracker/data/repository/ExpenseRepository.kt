package com.android.moneytracker.data.repository

import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import java.time.LocalDate
import java.time.LocalDateTime

interface ExpenseRepository {
    suspend fun saveExpense(expense: Expense)
    fun getExpensesForDateAndCategory(date: LocalDate, categoryId: Int) : List<Expense>
    fun getAllCategories() : List<Category>
}