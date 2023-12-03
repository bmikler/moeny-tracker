package com.android.moneytracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.ExpenseType
import com.android.moneytracker.model.Expense
import java.math.BigDecimal
import java.time.LocalDate

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): List<Category>
    @Query("SELECT * FROM expenses WHERE expenses.timestamp BETWEEN :startDate AND :endDate")
    fun getExpensesForDate(startDate: LocalDate, endDate: LocalDate) : List<Expense>
    @Query("SELECT categories.id, SUM(expenses.value) AS total FROM categories LEFT JOIN expenses ON categories.id = expenses.categoryId WHERE categories.type = :type AND expenses.timestamp BETWEEN :startDate AND :endDate")
    fun getExpensesForAnnualCategoriesForYear(startDate: LocalDate, endDate: LocalDate, type: ExpenseType = ExpenseType.ANNUAL): List<CategoryTotal>
    @Transaction
    fun getExpensesByCategory(dateRange: Pair<LocalDate, LocalDate>) : Map<Category, List<Expense>> {
        val categories = getCategories()
        val expenses = getExpensesForDate(dateRange.first, dateRange.second)
        return categories.associateWith { category -> expenses.filter { expense -> expense.categoryId == category.id } }
    }
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(expense: Expense)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(category: Category)

}

data class CategoryTotal(
    val id: Int,
    val total: BigDecimal
)