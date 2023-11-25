package com.android.moneytracker.data.repository

import com.android.moneytracker.data.CategoryTotal
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import com.android.moneytracker.ui.expenses.CategoryUi
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate


interface ExpenseRepository {
    suspend fun saveCategory(category: Category)
    suspend fun saveExpense(expense: Expense)
    fun getCategories(): List<Category>
    fun getExpensesForDate(dateRange: Pair<LocalDate, LocalDate>): List<Expense>
    fun getAnnualExpensesSummedByCategory(dateRange: Pair<LocalDate, LocalDate>): List<CategoryTotal>
}
