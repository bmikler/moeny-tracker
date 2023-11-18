package com.android.moneytracker.data.repository

import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import java.time.LocalDate


class InMemoryExpenseRepository : ExpenseRepository {

    private val expenses: MutableList<Expense> = mutableListOf()

    private val categories: List<Category> = listOf(
        Category(1, "Food"), Category(2, "Gas")
    )

    override suspend fun saveExpense(expense: Expense) {
        expenses.add(expense)
    }

    override fun getExpensesForDateAndCategory(date: LocalDate, categoryId: Int): List<Expense> {
        return expenses.filter { it.timestamp == date && it.categoryId == categoryId }
    }

    override fun getAllCategories(): List<Category> {
        return categories
    }

}