package com.android.moneytracker.data.repository

import com.android.moneytracker.data.CategoryTotal
import com.android.moneytracker.data.ExpenseDao
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import com.android.moneytracker.ui.expenses.CategoryUi
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

class OfflineExpenseRepository(private val expenseDao: ExpenseDao) : ExpenseRepository {
    override suspend fun saveCategory(category: Category) = expenseDao.save(category)
    override suspend fun saveExpense(expense: Expense) = expenseDao.save(expense)
    override fun getCategories(): List<Category> = expenseDao.getCategories()
    override fun getExpensesForDate(dateRange: Pair<LocalDate, LocalDate>): List<Expense> =
        expenseDao.getExpensesForDate(dateRange.first, dateRange.second)

    override fun getAnnualExpensesSummedByCategory(dateRange: Pair<LocalDate, LocalDate>): List<CategoryTotal> =
        expenseDao.getExpensesForAnnualCategoriesForYear(dateRange.first, dateRange.second)


}