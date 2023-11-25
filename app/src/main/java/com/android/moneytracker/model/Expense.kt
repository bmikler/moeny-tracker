package com.android.moneytracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate



@Entity(tableName = "expenses")
data class Expense (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val value: BigDecimal,
    val timestamp: LocalDate,
    val categoryId: Int
)