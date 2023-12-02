package com.android.moneytracker.ui.expense

import androidx.lifecycle.ViewModel
import java.time.LocalDate

class SharedDateViewModel : ViewModel() {

    var date: LocalDate = LocalDate.now()
        private set

    fun nextMonth() {
        date = date.plusMonths(1)
    }

    fun previousMonth() {
        date = date.minusMonths(1)
    }

}