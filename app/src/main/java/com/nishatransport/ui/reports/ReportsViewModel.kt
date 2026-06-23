package com.nishatransport.ui.reports

import androidx.lifecycle.*
import com.nishatransport.data.local.entity.Load
import com.nishatransport.data.repository.LoadRepository
import kotlinx.coroutines.launch

class ReportsViewModel(private val repository: LoadRepository) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadForMonth(year: Int, month: Int, onResult: (List<Load>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loads = mutableListOf<Load>()
                repository.getLoadsByMonth(year, month).collect { loads.addAll(it); return@collect }
                onResult(loads)
            } catch (e: Exception) {
                // Use alternative approach
                repository.getLoadsByDateRangeForExport(
                    getMonthStart(year, month),
                    getMonthEnd(year, month)
                ).let { onResult(it) }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadForYear(year: Int, onResult: (List<Load>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cal = java.util.Calendar.getInstance()
                cal.set(year, 0, 1, 0, 0, 0); cal.set(java.util.Calendar.MILLISECOND, 0)
                val start = cal.timeInMillis
                cal.set(year, 11, 31, 23, 59, 59)
                val end = cal.timeInMillis
                onResult(repository.getLoadsByDateRangeForExport(start, end))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadForDateRange(start: Long, end: Long, onResult: (List<Load>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                onResult(repository.getLoadsByDateRangeForExport(start, end))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAll(onResult: (List<Load>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                onResult(repository.getAllLoadsForExport())
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getMonthStart(year: Int, month: Int): Long {
        val cal = java.util.Calendar.getInstance()
        cal.set(year, month - 1, 1, 0, 0, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getMonthEnd(year: Int, month: Int): Long {
        val cal = java.util.Calendar.getInstance()
        cal.set(year, month - 1, 1)
        cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23)
        cal.set(java.util.Calendar.MINUTE, 59)
        cal.set(java.util.Calendar.SECOND, 59)
        return cal.timeInMillis
    }
}
