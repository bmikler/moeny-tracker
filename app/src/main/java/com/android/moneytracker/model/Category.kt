package com.android.moneytracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

enum class CostType {
    MONTHLY, ANNUAL
}

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val spendingLimit: BigDecimal = BigDecimal.ZERO,
    val type: CostType
)
