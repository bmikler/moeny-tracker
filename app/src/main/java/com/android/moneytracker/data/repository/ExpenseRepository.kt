package com.android.moneytracker.data.repository

import com.android.moneytracker.data.CategoryTotal
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import com.android.moneytracker.model.ExpenseType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


interface ExpenseRepository {
    suspend fun saveCategory(category: Category)
    suspend fun removeCategory(category: Category)
    fun getCategories() : Flow<List<Category>>
    suspend fun saveExpense(expense: Expense)
    suspend fun removeExpense(expense: Expense)
    fun getAnnualExpensesSummedByCategory(dateRange: Pair<LocalDate, LocalDate>): List<CategoryTotal>
    fun getCategoriesWithExpenses(dateRange: Pair<LocalDate, LocalDate>) : Map<Category, List<Expense>>
}
