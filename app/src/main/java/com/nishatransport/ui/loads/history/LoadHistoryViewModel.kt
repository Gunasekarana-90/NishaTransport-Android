package com.nishatransport.ui.loads.history

import androidx.lifecycle.*
import com.nishatransport.data.local.entity.Load
import com.nishatransport.data.repository.LoadRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(FlowPreview::class)
class LoadHistoryViewModel(private val repository: LoadRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _filterMode = MutableStateFlow(FilterMode.ALL)
    private val _filterMonth = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH) + 1)
    private val _filterYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    private val _filterStartDate = MutableStateFlow(0L)
    private val _filterEndDate = MutableStateFlow(0L)

    val filterMode: StateFlow<FilterMode> = _filterMode
    val filterYear: StateFlow<Int> = _filterYear
    val filterMonth: StateFlow<Int> = _filterMonth

    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> = _deleteResult

    val loads: Flow<List<Load>> = combine(
        _searchQuery.debounce(300),
        _filterMode,
        _filterMonth,
        _filterYear,
        _filterStartDate
    ) { query, mode, month, year, _ ->
        Triple(query, mode, Pair(month, year))
    }.flatMapLatest { (query, mode, monthYear) ->
        val (month, year) = monthYear
        when {
            query.isNotBlank() -> repository.searchLoads(query)
            mode == FilterMode.MONTH -> repository.getLoadsByMonth(year, month)
            mode == FilterMode.YEAR -> repository.getLoadsByYear(year)
            mode == FilterMode.DATE_RANGE -> repository.getLoadsByDateRange(
                _filterStartDate.value, _filterEndDate.value
            )
            else -> repository.getAllLoads()
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
        if (query.isNotBlank()) _filterMode.value = FilterMode.SEARCH
        else _filterMode.value = FilterMode.ALL
    }

    fun filterByMonth(year: Int, month: Int) {
        _filterYear.value = year
        _filterMonth.value = month
        _filterMode.value = FilterMode.MONTH
    }

    fun filterByYear(year: Int) {
        _filterYear.value = year
        _filterMode.value = FilterMode.YEAR
    }

    fun filterByDateRange(start: Long, end: Long) {
        _filterStartDate.value = start
        _filterEndDate.value = end
        _filterMode.value = FilterMode.DATE_RANGE
    }

    fun clearFilter() {
        _searchQuery.value = ""
        _filterMode.value = FilterMode.ALL
    }

    fun deleteLoad(load: Load) {
        viewModelScope.launch {
            try {
                repository.deleteLoad(load)
                _deleteResult.value = true
            } catch (e: Exception) {
                _deleteResult.value = false
            }
        }
    }

    enum class FilterMode { ALL, SEARCH, MONTH, YEAR, DATE_RANGE }
}
