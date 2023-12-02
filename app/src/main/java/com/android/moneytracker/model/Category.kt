package com.android.moneytracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

enum class ExpenseType {
    MONTHLY, ANNUAL
}

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val spendingLimit: BigDecimal = BigDecimal.ZERO,
    val type: ExpenseType
)
