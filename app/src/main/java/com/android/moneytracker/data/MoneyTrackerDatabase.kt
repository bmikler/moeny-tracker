package com.android.moneytracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.Expense

@Database(entities = [Expense::class, Category::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MoneyTrackerDatabase : RoomDatabase(){

    abstract fun expenseDao() : ExpenseDao

    companion object {
        @Volatile
        private var Instance: MoneyTrackerDatabase? = null

        fun getDatabase(context: Context): MoneyTrackerDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MoneyTrackerDatabase::class.java, "expense_databse")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }


}