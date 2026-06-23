package com.nishatransport.ui.dashboard

import androidx.lifecycle.*
import com.nishatransport.data.repository.DaySummary
import com.nishatransport.data.repository.LoadRepository
import com.nishatransport.data.repository.MonthSummary
import com.nishatransport.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardViewModel(private val repository: LoadRepository) : ViewModel() {

    val recentLoads = repository.getRecentLoads()
    val totalLoadCount = repository.getTotalLoadCount()

    private val _todaySummary = MutableLiveData<DaySummary>()
    val todaySummary: LiveData<DaySummary> = _todaySummary

    private val _monthSummary = MutableLiveData<MonthSummary>()
    val monthSummary: LiveData<MonthSummary> = _monthSummary

    private val _yearProfit = MutableLiveData<Double>()
    val yearProfit: LiveData<Double> = _yearProfit

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cal = Calendar.getInstance()
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH) + 1

                _todaySummary.value = repository.getTodaySummary()
                _monthSummary.value = repository.getMonthSummary(year, month)

                val yearSummary = repository.getYearSummary(year)
                _yearProfit.value = yearSummary.profit
            } finally {
                _isLoading.value = false
            }
        }
    }
}
