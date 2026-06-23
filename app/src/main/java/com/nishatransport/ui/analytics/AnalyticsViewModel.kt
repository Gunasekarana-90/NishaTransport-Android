package com.nishatransport.ui.analytics

import androidx.lifecycle.*
import com.nishatransport.data.local.dao.MonthlyTrendData
import com.nishatransport.data.repository.LoadRepository
import com.nishatransport.data.repository.MonthSummary
import com.nishatransport.data.repository.YearSummary
import kotlinx.coroutines.launch
import java.util.Calendar

class AnalyticsViewModel(private val repository: LoadRepository) : ViewModel() {

    private val _monthSummary = MutableLiveData<MonthSummary>()
    val monthSummary: LiveData<MonthSummary> = _monthSummary

    private val _yearSummary = MutableLiveData<YearSummary>()
    val yearSummary: LiveData<YearSummary> = _yearSummary

    private val _monthlyTrend = MutableLiveData<List<MonthlyTrendData>>()
    val monthlyTrend: LiveData<List<MonthlyTrendData>> = _monthlyTrend

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val cal = Calendar.getInstance()
    var selectedYear: Int = cal.get(Calendar.YEAR)
    var selectedMonth: Int = cal.get(Calendar.MONTH) + 1

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _monthSummary.value = repository.getMonthSummary(selectedYear, selectedMonth)
                _yearSummary.value = repository.getYearSummary(selectedYear)
                _monthlyTrend.value = repository.getMonthlyTrend()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectMonth(year: Int, month: Int) {
        selectedYear = year
        selectedMonth = month
        viewModelScope.launch {
            _monthSummary.value = repository.getMonthSummary(year, month)
        }
    }

    fun selectYear(year: Int) {
        selectedYear = year
        viewModelScope.launch {
            _yearSummary.value = repository.getYearSummary(year)
        }
    }
}
