package com.android.moneytracker.data.repository

import com.android.moneytracker.data.CategoryTotal
import com.android.moneytracker.data.ExpenseDao
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import com.android.moneytracker.model.ExpenseType
import kotlinx.coroutines.flow.Flow

import java.time.LocalDate

class OfflineExpenseRepository(private val expenseDao: ExpenseDao) : ExpenseRepository {
    override suspend fun saveCategory(category: Category) = expenseDao.save(category)
    override suspend fun saveExpense(expense: Expense) = expenseDao.save(expense)
    override suspend fun removeExpense(expense: Expense) = expenseDao.removeExpense(expense)
    override suspend fun removeCategory(category: Category) = expenseDao.removeCategory(category)
    override fun getCategories(): Flow<List<Category>> = expenseDao.getCategoriesFlow()

    override fun getCategoriesWithExpenses(dateRange: Pair<LocalDate, LocalDate>): Map<Category, List<Expense>> = expenseDao.getExpensesByCategory(dateRange)
    override fun getAnnualExpensesSummedByCategory(dateRange: Pair<LocalDate, LocalDate>): List<CategoryTotal> =
        expenseDao.getExpensesForAnnualCategoriesForYear(dateRange.first, dateRange.second)


}