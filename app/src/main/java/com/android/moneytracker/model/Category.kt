package com.android.moneytracker.model

enum class CostType {
    IRREGULAR, CONSTANT
}

data class Category (
    val id: Int,
    val name: String,
    val type: CostType
)
