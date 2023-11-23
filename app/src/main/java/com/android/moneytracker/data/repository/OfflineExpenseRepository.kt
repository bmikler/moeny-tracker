package com.android.moneytracker.data.repository

import com.android.moneytracker.data.ExpenseDao
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.flow.Flow

class OfflineExpenseRepository(private val expenseDao: ExpenseDao) : ExpenseRepository {
    override suspend fun saveCategory(category: Category) = expenseDao.save(category)

    override suspend fun saveExpense(expense: Expense) = expenseDao.save(expense)

    override fun getCategoriesAndExpenses(): Flow<Map<Category, List<Expense>>> = expenseDao.getExpensesByCategory()

}