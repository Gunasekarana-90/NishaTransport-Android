package com.nishatransport.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nishatransport.data.local.entity.Load
import kotlinx.coroutines.flow.Flow

@Dao
interface LoadDao {

    // ───────── INSERT / UPDATE / DELETE ─────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoad(load: Load): Long

    @Update
    suspend fun updateLoad(load: Load)

    @Delete
    suspend fun deleteLoad(load: Load)

    @Query("DELETE FROM loads WHERE id = :id")
    suspend fun deleteLoadById(id: Long)

    // ───────── GET ALL / SINGLE ─────────

    @Query("SELECT * FROM loads ORDER BY date DESC")
    fun getAllLoads(): Flow<List<Load>>

    @Query("SELECT * FROM loads ORDER BY date DESC LIMIT 5")
    fun getRecentLoads(): LiveData<List<Load>>

    @Query("SELECT * FROM loads WHERE id = :id")
    suspend fun getLoadById(id: Long): Load?

    // ───────── SEARCH ─────────

    @Query("""
        SELECT * FROM loads 
        WHERE (fromLocation LIKE '%' || :query || '%' 
            OR toLocation LIKE '%' || :query || '%'
            OR vehicleNumber LIKE '%' || :query || '%')
        ORDER BY date DESC
    """)
    fun searchLoads(query: String): Flow<List<Load>>

    // ───────── DATE FILTER ─────────

    @Query("""
        SELECT * FROM loads 
        WHERE date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    fun getLoadsByDateRange(startDate: Long, endDate: Long): Flow<List<Load>>

    // ───────── MONTH FILTER ─────────

    @Query("""
        SELECT * FROM loads 
        WHERE strftime('%Y-%m', date/1000, 'unixepoch') = :yearMonth
        ORDER BY date DESC
    """)
    fun getLoadsByMonth(yearMonth: String): Flow<List<Load>>  // format: "2024-01"

    // ───────── YEAR FILTER ─────────

    @Query("""
        SELECT * FROM loads 
        WHERE strftime('%Y', date/1000, 'unixepoch') = :year
        ORDER BY date DESC
    """)
    fun getLoadsByYear(year: String): Flow<List<Load>>

    // ───────── TODAY SUMMARY ─────────

    @Query("""
        SELECT COALESCE(SUM(loadPrice), 0) FROM loads 
        WHERE date BETWEEN :startOfDay AND :endOfDay
    """)
    suspend fun getTodayRevenue(startOfDay: Long, endOfDay: Long): Double

    @Query("""
        SELECT COALESCE(SUM(profit), 0) FROM loads 
        WHERE date BETWEEN :startOfDay AND :endOfDay
    """)
    suspend fun getTodayProfit(startOfDay: Long, endOfDay: Long): Double

    // ───────── MONTH SUMMARY ─────────

    @Query("""
        SELECT COALESCE(SUM(loadPrice), 0) FROM loads 
        WHERE date BETWEEN :startOfMonth AND :endOfMonth
    """)
    suspend fun getMonthRevenue(startOfMonth: Long, endOfMonth: Long): Double

    @Query("""
        SELECT COALESCE(SUM(totalExpense), 0) FROM loads 
        WHERE date BETWEEN :startOfMonth AND :endOfMonth
    """)
    suspend fun getMonthExpense(startOfMonth: Long, endOfMonth: Long): Double

    @Query("""
        SELECT COALESCE(SUM(profit), 0) FROM loads 
        WHERE date BETWEEN :startOfMonth AND :endOfMonth
    """)
    suspend fun getMonthProfit(startOfMonth: Long, endOfMonth: Long): Double

    @Query("""
        SELECT COUNT(*) FROM loads 
        WHERE date BETWEEN :startOfMonth AND :endOfMonth
    """)
    suspend fun getMonthLoadCount(startOfMonth: Long, endOfMonth: Long): Int

    // ───────── YEAR SUMMARY ─────────

    @Query("""
        SELECT COALESCE(SUM(loadPrice), 0) FROM loads 
        WHERE date BETWEEN :startOfYear AND :endOfYear
    """)
    suspend fun getYearRevenue(startOfYear: Long, endOfYear: Long): Double

    @Query("""
        SELECT COALESCE(SUM(totalExpense), 0) FROM loads 
        WHERE date BETWEEN :startOfYear AND :endOfYear
    """)
    suspend fun getYearExpense(startOfYear: Long, endOfYear: Long): Double

    @Query("""
        SELECT COALESCE(SUM(profit), 0) FROM loads 
        WHERE date BETWEEN :startOfYear AND :endOfYear
    """)
    suspend fun getYearProfit(startOfYear: Long, endOfYear: Long): Double

    @Query("""
        SELECT COUNT(*) FROM loads 
        WHERE date BETWEEN :startOfYear AND :endOfYear
    """)
    suspend fun getYearLoadCount(startOfYear: Long, endOfYear: Long): Int

    // ───────── TOTAL COUNTS ─────────

    @Query("SELECT COUNT(*) FROM loads")
    fun getTotalLoadCount(): LiveData<Int>

    @Query("SELECT COALESCE(SUM(loadPrice), 0) FROM loads")
    fun getTotalRevenue(): LiveData<Double>

    @Query("SELECT COALESCE(SUM(profit), 0) FROM loads")
    fun getTotalProfit(): LiveData<Double>

    // ───────── ANALYTICS - MONTHLY TREND (last 12 months) ─────────

    @Query("""
        SELECT 
            strftime('%Y-%m', date/1000, 'unixepoch') as month,
            COALESCE(SUM(loadPrice), 0) as revenue,
            COALESCE(SUM(totalExpense), 0) as expense,
            COALESCE(SUM(profit), 0) as profit,
            COUNT(*) as loadCount
        FROM loads
        WHERE date >= :since
        GROUP BY month
        ORDER BY month ASC
    """)
    suspend fun getMonthlyTrend(since: Long): List<MonthlyTrendData>

    // ───────── ALL LOADS FOR EXPORT ─────────

    @Query("SELECT * FROM loads ORDER BY date DESC")
    suspend fun getAllLoadsForExport(): List<Load>

    @Query("""
        SELECT * FROM loads 
        WHERE date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    suspend fun getLoadsByDateRangeForExport(startDate: Long, endDate: Long): List<Load>
}

// ───────── DATA CLASSES FOR QUERY RESULTS ─────────

data class MonthlyTrendData(
    val month: String,        // "2024-01"
    val revenue: Double,
    val expense: Double,
    val profit: Double,
    val loadCount: Int
)
