package com.android.moneytracker.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class Expense (
    val id: Int,
    val description: String,
    val value: BigDecimal,
    val timestamp: LocalDate,
    val categoryId: Int
)