package com.android.moneytracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("SELECT * FROM categories LEFT JOIN expenses ON expenses.categoryId = categories.id")
    fun getExpensesByCategory() : Map<Category, List<Expense>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(expense: Expense)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(category: Category)

}

data class CategoryTotal(
    val id: Int,
    val total: BigDecimal
)