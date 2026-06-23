package com.nishatransport.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setupVertical() {
    layoutManager = LinearLayoutManager(context)
    setHasFixedSize(true)
}

fun Double.toRupees(): String = CurrencyUtils.format(this)

fun Long.toDisplayDate(): String = DateUtils.formatDate(this)

fun String.toDoubleOrZero(): Double = this.toDoubleOrNull() ?: 0.0
