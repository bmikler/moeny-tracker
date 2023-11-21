package com.android.moneytracker.model

import java.math.BigDecimal

enum class CostType {
    IRREGULAR, CONSTANT
}

data class Category (
    val id: Int,
    val name: String,
    val spendingLimit: BigDecimal = BigDecimal.ZERO,
    val type: CostType
)
