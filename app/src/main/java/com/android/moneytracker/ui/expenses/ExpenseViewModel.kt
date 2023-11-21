package com.android.moneytracker.ui.expenses

import androidx.lifecycle.ViewModel
import com.android.moneytracker.data.repository.ExpenseRepository
import com.android.moneytracker.model.Category
import com.android.moneytracker.model.CostType
import com.android.moneytracker.model.Expense
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    private val expanses : Map<Category, List<Expense>> = mapOf(
        Category(1, "test1", BigDecimal.TEN, CostType.IRREGULAR) to listOf(),
        Category(2, "test2", BigDecimal.TEN, CostType.IRREGULAR) to listOf()
    )

    fun getData(date: LocalDate) : Map<Category, List<Expense>> {
        return expanses
    }

}