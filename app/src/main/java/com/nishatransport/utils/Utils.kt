package com.nishatransport.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val monthYearFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    private val fullFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val yearMonthKey = SimpleDateFormat("yyyy-MM", Locale.getDefault())

    fun formatDate(millis: Long): String = displayFormat.format(Date(millis))

    fun formatMonthYear(millis: Long): String = monthYearFormat.format(Date(millis))

    fun formatFull(millis: Long): String = fullFormat.format(Date(millis))

    fun parseDate(str: String): Long? = try {
        displayFormat.parse(str)?.time
    } catch (e: Exception) {
        null
    }

    fun startOfDay(millis: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun endOfDay(millis: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

    fun currentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

    fun monthName(month: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, month - 1)
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.time)
    }

    fun shortMonthName(yearMonth: String): String {
        // yearMonth = "2024-01"
        return try {
            val parts = yearMonth.split("-")
            val m = parts[1].toInt()
            val months = listOf("Jan","Feb","Mar","Apr","May","Jun",
                "Jul","Aug","Sep","Oct","Nov","Dec")
            months.getOrElse(m - 1) { yearMonth }
        } catch (e: Exception) { yearMonth }
    }
}

object CurrencyUtils {
    fun format(amount: Double): String {
        return "₹%,.0f".format(amount)
    }

    fun formatWithDecimal(amount: Double): String {
        return "₹%,.2f".format(amount)
    }
}
