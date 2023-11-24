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
    fun bigDecimalToString(input: BigDecimal): String {
        return input.toPlainString() ?: ""
    }

    @TypeConverter
    fun stringToBigDecimal(input: String): BigDecimal {
        if (input.isBlank()) return BigDecimal.valueOf(0.0)
        return input.toBigDecimalOrNull() ?: BigDecimal.valueOf(0.0)
    }

}