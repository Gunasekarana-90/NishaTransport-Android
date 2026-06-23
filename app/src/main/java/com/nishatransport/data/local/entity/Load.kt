package com.nishatransport.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "loads")
data class Load(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Basic Information
    val date: Long = System.currentTimeMillis(),      // stored as epoch millis
    val fromLocation: String = "",
    val toLocation: String = "",
    val vehicleNumber: String = "",
    val notes: String = "",

    // Revenue
    val loadPrice: Double = 0.0,

    // Expenses
    val loadingCharge: Double = 0.0,
    val dieselCost: Double = 0.0,
    val policeExpense: Double = 0.0,
    val tollFee: Double = 0.0,
    val driverCharge: Double = 0.0,
    val unloadingCharge: Double = 0.0,
    val otherExpense: Double = 0.0,

    // Calculated (stored for quick queries)
    val totalExpense: Double = 0.0,
    val profit: Double = 0.0,

    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Serializable {

    companion object {
        fun calculateTotalExpense(
            loading: Double, diesel: Double, police: Double,
            toll: Double, driver: Double, unloading: Double, other: Double
        ): Double = loading + diesel + police + toll + driver + unloading + other

        fun calculateProfit(loadPrice: Double, totalExpense: Double): Double =
            loadPrice - totalExpense
    }
}
