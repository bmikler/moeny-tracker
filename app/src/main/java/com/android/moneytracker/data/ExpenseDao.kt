package com.android.moneytracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<Category>>
    @Query("SELECT * FROM expenses WHERE expenses.timestamp BETWEEN :startDate AND :endDate")
    fun getExpensesForDate(startDate: LocalDate, endDate: LocalDate) : Flow<List<Expense>>

    @Query("SELECT * FROM categories LEFT JOIN expenses ON expenses.categoryId = categories.id")
    fun getExpensesByCategory() : Flow<Map<Category, List<Expense>>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(expense: Expense)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(category: Category)

}
