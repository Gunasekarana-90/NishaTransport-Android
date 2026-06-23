package com.nishatransport.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nishatransport.data.repository.LoadRepository
import com.nishatransport.ui.analytics.AnalyticsViewModel
import com.nishatransport.ui.dashboard.DashboardViewModel
import com.nishatransport.ui.loads.add.AddLoadViewModel
import com.nishatransport.ui.loads.history.LoadHistoryViewModel
import com.nishatransport.ui.reports.ReportsViewModel

class ViewModelFactory(private val repository: LoadRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(repository) as T
            modelClass.isAssignableFrom(AddLoadViewModel::class.java) ->
                AddLoadViewModel(repository) as T
            modelClass.isAssignableFrom(LoadHistoryViewModel::class.java) ->
                LoadHistoryViewModel(repository) as T
            modelClass.isAssignableFrom(AnalyticsViewModel::class.java) ->
                AnalyticsViewModel(repository) as T
            modelClass.isAssignableFrom(ReportsViewModel::class.java) ->
                ReportsViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}
