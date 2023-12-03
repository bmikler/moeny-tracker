package com.android.moneytracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

enum class ExpenseType {
    MONTHLY, ANNUAL
}



@Parcelize
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = " ",
    val spendingLimit: BigDecimal = BigDecimal.ZERO,
    val type: ExpenseType = ExpenseType.MONTHLY
) : Parcelable


