package com.android.moneytracker.data


import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate


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