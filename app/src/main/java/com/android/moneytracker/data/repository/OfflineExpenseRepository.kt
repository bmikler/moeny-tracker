package com.android.moneytracker.data.repository

import com.android.moneytracker.data.ExpenseDao
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class OfflineExpenseRepository(private val expenseDao: ExpenseDao) : ExpenseRepository {
    override suspend fun saveCategory(category: Category) = expenseDao.save(category)

    override suspend fun saveExpense(expense: Expense) = expenseDao.save(expense)
    override fun getCategories(): Flow<List<Category>> = expenseDao.getCategories()

    override fun getExpensesForDate(dateRange: Pair<LocalDate, LocalDate>): Flow<List<Expense>> = expenseDao.getExpensesForDate(dateRange.first, dateRange.second)


}