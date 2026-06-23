package com.nishatransport.ui.loads.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nishatransport.NishaTransportApp
import com.nishatransport.R
import com.nishatransport.data.local.entity.Load
import com.nishatransport.databinding.ActivityAddLoadBinding
import com.nishatransport.utils.CurrencyUtils
import com.nishatransport.utils.DateUtils
import com.nishatransport.utils.ViewModelFactory
import java.util.Calendar

class AddLoadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLoadBinding

    private val viewModel: AddLoadViewModel by viewModels {
        ViewModelFactory((application as NishaTransportApp).repository)
    }

    private var selectedDate: Long = System.currentTimeMillis()
    private var editLoadId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Check if editing existing load
        editLoadId = intent.getLongExtra("edit_load_id", -1L)

        setupDatePicker()
        setupTextWatchers()
        setupObservers()
        setupSaveButton()

        if (editLoadId != -1L) {
            supportActionBar?.title = "Edit Load"
            loadExistingLoad(editLoadId)
        } else {
            supportActionBar?.title = "Add New Load"
            updateDateDisplay()
        }
    }

    private fun loadExistingLoad(id: Long) {
        // Load data from intent if passed directly
        val load = intent.getSerializableExtra("load_data") as? Load
        load?.let {
            populateFields(it)
            viewModel.setEditLoad(it)
        }
    }

    private fun populateFields(load: Load) {
        selectedDate = load.date
        updateDateDisplay()

        binding.etFrom.setText(load.fromLocation)
        binding.etTo.setText(load.toLocation)
        binding.etVehicleNumber.setText(load.vehicleNumber)
        binding.etNotes.setText(load.notes)
        binding.etLoadPrice.setText(if (load.loadPrice > 0) load.loadPrice.toLong().toString() else "")
        binding.etLoadingCharge.setText(if (load.loadingCharge > 0) load.loadingCharge.toLong().toString() else "")
        binding.etDieselCost.setText(if (load.dieselCost > 0) load.dieselCost.toLong().toString() else "")
        binding.etPoliceExpense.setText(if (load.policeExpense > 0) load.policeExpense.toLong().toString() else "")
        binding.etTollFee.setText(if (load.tollFee > 0) load.tollFee.toLong().toString() else "")
        binding.etDriverCharge.setText(if (load.driverCharge > 0) load.driverCharge.toLong().toString() else "")
        binding.etUnloadingCharge.setText(if (load.unloadingCharge > 0) load.unloadingCharge.toLong().toString() else "")
        binding.etOtherExpense.setText(if (load.otherExpense > 0) load.otherExpense.toLong().toString() else "")
    }

    private fun setupDatePicker() {
        binding.tvDate.setOnClickListener { showDatePicker() }
        binding.btnPickDate.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                cal.set(year, month, dayOfMonth)
                selectedDate = cal.timeInMillis
                updateDateDisplay()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateDisplay() {
        binding.tvDate.text = DateUtils.formatDate(selectedDate)
    }

    private fun setupTextWatchers() {
        binding.etLoadPrice.addTextChangedListener(makeWatcher { viewModel.updateLoadPrice(it) })
        binding.etLoadingCharge.addTextChangedListener(makeWatcher { viewModel.updateLoadingCharge(it) })
        binding.etDieselCost.addTextChangedListener(makeWatcher { viewModel.updateDieselCost(it) })
        binding.etPoliceExpense.addTextChangedListener(makeWatcher { viewModel.updatePoliceExpense(it) })
        binding.etTollFee.addTextChangedListener(makeWatcher { viewModel.updateTollFee(it) })
        binding.etDriverCharge.addTextChangedListener(makeWatcher { viewModel.updateDriverCharge(it) })
        binding.etUnloadingCharge.addTextChangedListener(makeWatcher { viewModel.updateUnloadingCharge(it) })
        binding.etOtherExpense.addTextChangedListener(makeWatcher { viewModel.updateOtherExpense(it) })
    }

    private fun makeWatcher(onUpdate: (Double) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val value = s?.toString()?.toDoubleOrNull() ?: 0.0
                onUpdate(value)
            }
        }
    }

    private fun setupObservers() {
        viewModel.totalExpense.observe(this) { total ->
            binding.tvTotalExpense.text = CurrencyUtils.format(total)
        }

        viewModel.profit.observe(this) { profit ->
            binding.tvProfit.text = CurrencyUtils.format(profit)
            val color = if (profit >= 0) {
                ContextCompat.getColor(this, R.color.profit_positive)
            } else {
                ContextCompat.getColor(this, R.color.profit_negative)
            }
            binding.tvProfit.setTextColor(color)
            binding.tvProfitLabel.setTextColor(color)
        }

        viewModel.saveResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Load saved successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.btnSave.isEnabled = !loading
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            if (validate()) {
                viewModel.saveLoad(
                    date = selectedDate,
                    fromLocation = binding.etFrom.text.toString().trim(),
                    toLocation = binding.etTo.text.toString().trim(),
                    vehicleNumber = binding.etVehicleNumber.text.toString().trim(),
                    notes = binding.etNotes.text.toString().trim()
                )
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (binding.etFrom.text.isNullOrBlank()) {
            binding.tilFrom.error = "From location is required"
            isValid = false
        } else {
            binding.tilFrom.error = null
        }

        if (binding.etTo.text.isNullOrBlank()) {
            binding.tilTo.error = "To location is required"
            isValid = false
        } else {
            binding.tilTo.error = null
        }

        val loadPrice = binding.etLoadPrice.text.toString().toDoubleOrNull()
        if (loadPrice == null || loadPrice <= 0) {
            binding.tilLoadPrice.error = "Load price is required and must be greater than 0"
            isValid = false
        } else {
            binding.tilLoadPrice.error = null
        }

        return isValid
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
