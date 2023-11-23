package com.android.moneytracker.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM categories LEFT JOIN expenses ON expenses.categoryId = categories.id")
    fun getExpensesByCategory() : Flow<Map<Category, List<Expense>>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(expense: Expense)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(category: Category)

}
