package com.nishatransport.ui.loads.add

import androidx.lifecycle.*
import com.nishatransport.data.local.entity.Load
import com.nishatransport.data.repository.LoadRepository
import kotlinx.coroutines.launch

class AddLoadViewModel(private val repository: LoadRepository) : ViewModel() {

    // Live calculation fields
    private val _loadingCharge = MutableLiveData(0.0)
    private val _dieselCost = MutableLiveData(0.0)
    private val _policeExpense = MutableLiveData(0.0)
    private val _tollFee = MutableLiveData(0.0)
    private val _driverCharge = MutableLiveData(0.0)
    private val _unloadingCharge = MutableLiveData(0.0)
    private val _otherExpense = MutableLiveData(0.0)
    private val _loadPrice = MutableLiveData(0.0)

    private val _totalExpense = MutableLiveData(0.0)
    val totalExpense: LiveData<Double> = _totalExpense

    private val _profit = MutableLiveData(0.0)
    val profit: LiveData<Double> = _profit

    private val _saveResult = MutableLiveData<Result<Long>>()
    val saveResult: LiveData<Result<Long>> = _saveResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // For edit mode
    private var editingLoad: Load? = null

    fun setEditLoad(load: Load) {
        editingLoad = load
        _loadPrice.value = load.loadPrice
        _loadingCharge.value = load.loadingCharge
        _dieselCost.value = load.dieselCost
        _policeExpense.value = load.policeExpense
        _tollFee.value = load.tollFee
        _driverCharge.value = load.driverCharge
        _unloadingCharge.value = load.unloadingCharge
        _otherExpense.value = load.otherExpense
        recalculate()
    }

    fun updateLoadPrice(value: Double) { _loadPrice.value = value; recalculate() }
    fun updateLoadingCharge(value: Double) { _loadingCharge.value = value; recalculate() }
    fun updateDieselCost(value: Double) { _dieselCost.value = value; recalculate() }
    fun updatePoliceExpense(value: Double) { _policeExpense.value = value; recalculate() }
    fun updateTollFee(value: Double) { _tollFee.value = value; recalculate() }
    fun updateDriverCharge(value: Double) { _driverCharge.value = value; recalculate() }
    fun updateUnloadingCharge(value: Double) { _unloadingCharge.value = value; recalculate() }
    fun updateOtherExpense(value: Double) { _otherExpense.value = value; recalculate() }

    private fun recalculate() {
        val total = Load.calculateTotalExpense(
            _loadingCharge.value ?: 0.0,
            _dieselCost.value ?: 0.0,
            _policeExpense.value ?: 0.0,
            _tollFee.value ?: 0.0,
            _driverCharge.value ?: 0.0,
            _unloadingCharge.value ?: 0.0,
            _otherExpense.value ?: 0.0
        )
        _totalExpense.value = total
        _profit.value = Load.calculateProfit(_loadPrice.value ?: 0.0, total)
    }

    fun saveLoad(
        date: Long,
        fromLocation: String,
        toLocation: String,
        vehicleNumber: String,
        notes: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val totalExp = _totalExpense.value ?: 0.0
                val profitVal = _profit.value ?: 0.0
                val now = System.currentTimeMillis()

                val load = if (editingLoad != null) {
                    editingLoad!!.copy(
                        date = date,
                        fromLocation = fromLocation,
                        toLocation = toLocation,
                        vehicleNumber = vehicleNumber,
                        notes = notes,
                        loadPrice = _loadPrice.value ?: 0.0,
                        loadingCharge = _loadingCharge.value ?: 0.0,
                        dieselCost = _dieselCost.value ?: 0.0,
                        policeExpense = _policeExpense.value ?: 0.0,
                        tollFee = _tollFee.value ?: 0.0,
                        driverCharge = _driverCharge.value ?: 0.0,
                        unloadingCharge = _unloadingCharge.value ?: 0.0,
                        otherExpense = _otherExpense.value ?: 0.0,
                        totalExpense = totalExp,
                        profit = profitVal,
                        updatedAt = now
                    )
                } else {
                    Load(
                        date = date,
                        fromLocation = fromLocation,
                        toLocation = toLocation,
                        vehicleNumber = vehicleNumber,
                        notes = notes,
                        loadPrice = _loadPrice.value ?: 0.0,
                        loadingCharge = _loadingCharge.value ?: 0.0,
                        dieselCost = _dieselCost.value ?: 0.0,
                        policeExpense = _policeExpense.value ?: 0.0,
                        tollFee = _tollFee.value ?: 0.0,
                        driverCharge = _driverCharge.value ?: 0.0,
                        unloadingCharge = _unloadingCharge.value ?: 0.0,
                        otherExpense = _otherExpense.value ?: 0.0,
                        totalExpense = totalExp,
                        profit = profitVal,
                        createdAt = now,
                        updatedAt = now
                    )
                }

                val id = if (editingLoad != null) {
                    repository.updateLoad(load)
                    load.id
                } else {
                    repository.insertLoad(load)
                }

                _saveResult.value = Result.success(id)
            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
