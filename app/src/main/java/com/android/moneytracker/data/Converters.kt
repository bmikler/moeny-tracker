package com.android.moneytracker.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {

    @TypeConverter
    fun fromString(value: String): LocalDate {
        return LocalDate.parse(value)
    }
    @TypeConverter
    fun dateToString(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun bigDecimalToString(input: BigDecimal): Double {
        return input.toDouble()
    }

    @TypeConverter
    fun stringToBigDecimal(input: Double): BigDecimal {
        return BigDecimal(input)
    }

}