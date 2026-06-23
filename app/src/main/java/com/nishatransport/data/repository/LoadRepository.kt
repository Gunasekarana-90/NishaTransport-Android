package com.nishatransport.data.repository

import com.nishatransport.data.local.dao.LoadDao
import com.nishatransport.data.local.dao.MonthlyTrendData
import com.nishatransport.data.local.entity.Load
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class LoadRepository(private val loadDao: LoadDao) {

    // ───────── CRUD ─────────

    suspend fun insertLoad(load: Load): Long = loadDao.insertLoad(load)

    suspend fun updateLoad(load: Load) = loadDao.updateLoad(load)

    suspend fun deleteLoad(load: Load) = loadDao.deleteLoad(load)

    suspend fun deleteLoadById(id: Long) = loadDao.deleteLoadById(id)

    suspend fun getLoadById(id: Long): Load? = loadDao.getLoadById(id)

    // ───────── STREAMS ─────────

    fun getAllLoads(): Flow<List<Load>> = loadDao.getAllLoads()

    fun getRecentLoads() = loadDao.getRecentLoads()

    fun searchLoads(query: String): Flow<List<Load>> = loadDao.searchLoads(query)

    fun getLoadsByDateRange(startDate: Long, endDate: Long): Flow<List<Load>> =
        loadDao.getLoadsByDateRange(startDate, endDate)

    fun getLoadsByMonth(year: Int, month: Int): Flow<List<Load>> {
        val yearMonth = "%04d-%02d".format(year, month)
        return loadDao.getLoadsByMonth(yearMonth)
    }

    fun getLoadsByYear(year: Int): Flow<List<Load>> =
        loadDao.getLoadsByYear(year.toString())

    // ───────── LIVE DATA ─────────

    fun getTotalLoadCount() = loadDao.getTotalLoadCount()
    fun getTotalRevenue() = loadDao.getTotalRevenue()
    fun getTotalProfit() = loadDao.getTotalProfit()

    // ───────── TODAY SUMMARY ─────────

    suspend fun getTodaySummary(): DaySummary {
        val (start, end) = todayRange()
        return DaySummary(
            revenue = loadDao.getTodayRevenue(start, end),
            profit = loadDao.getTodayProfit(start, end)
        )
    }

    // ───────── MONTH SUMMARY ─────────

    suspend fun getMonthSummary(year: Int, month: Int): MonthSummary {
        val (start, end) = monthRange(year, month)
        return MonthSummary(
            revenue = loadDao.getMonthRevenue(start, end),
            expense = loadDao.getMonthExpense(start, end),
            profit = loadDao.getMonthProfit(start, end),
            loadCount = loadDao.getMonthLoadCount(start, end)
        )
    }

    // ───────── YEAR SUMMARY ─────────

    suspend fun getYearSummary(year: Int): YearSummary {
        val (start, end) = yearRange(year)
        return YearSummary(
            revenue = loadDao.getYearRevenue(start, end),
            expense = loadDao.getYearExpense(start, end),
            profit = loadDao.getYearProfit(start, end),
            loadCount = loadDao.getYearLoadCount(start, end)
        )
    }

    // ───────── ANALYTICS ─────────

    suspend fun getMonthlyTrend(): List<MonthlyTrendData> {
        val since = Calendar.getInstance().apply {
            add(Calendar.MONTH, -11)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return loadDao.getMonthlyTrend(since)
    }

    // ───────── EXPORT ─────────

    suspend fun getAllLoadsForExport(): List<Load> = loadDao.getAllLoadsForExport()

    suspend fun getLoadsByDateRangeForExport(startDate: Long, endDate: Long): List<Load> =
        loadDao.getLoadsByDateRangeForExport(startDate, endDate)

    // ───────── HELPERS ─────────

    private fun todayRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val end = cal.timeInMillis
        return Pair(start, end)
    }

    private fun monthRange(year: Int, month: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val end = cal.timeInMillis
        return Pair(start, end)
    }

    private fun yearRange(year: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(year, 0, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(year, 11, 31, 23, 59, 59)
        val end = cal.timeInMillis
        return Pair(start, end)
    }
}

// ───────── SUMMARY DATA CLASSES ─────────

data class DaySummary(
    val revenue: Double = 0.0,
    val profit: Double = 0.0
)

data class MonthSummary(
    val revenue: Double = 0.0,
    val expense: Double = 0.0,
    val profit: Double = 0.0,
    val loadCount: Int = 0
)

data class YearSummary(
    val revenue: Double = 0.0,
    val expense: Double = 0.0,
    val profit: Double = 0.0,
    val loadCount: Int = 0
)
