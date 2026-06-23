package com.nishatransport.ui.loads.detail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.nishatransport.NishaTransportApp
import com.nishatransport.R
import com.nishatransport.databinding.ActivityLoadDetailBinding
import com.nishatransport.ui.loads.add.AddLoadActivity
import com.nishatransport.utils.CurrencyUtils
import com.nishatransport.utils.DateUtils
import kotlinx.coroutines.launch

class LoadDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadDetailBinding
    private var loadId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Load Details"

        loadId = intent.getLongExtra("load_id", -1L)
        if (loadId == -1L) { finish(); return }

        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            val repo = (application as NishaTransportApp).repository
            val load = repo.getLoadById(loadId)

            if (load == null) {
                Toast.makeText(this@LoadDetailActivity, "Load not found", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            // Basic Info
            binding.tvDate.text = DateUtils.formatFull(load.date)
            binding.tvRoute.text = "${load.fromLocation}  →  ${load.toLocation}"
            binding.tvVehicle.text = load.vehicleNumber.ifBlank { "Not specified" }
            binding.tvNotes.text = load.notes.ifBlank { "No notes" }
            binding.tvLoadId.text = "#${load.id}"

            // Revenue
            binding.tvLoadPrice.text = CurrencyUtils.format(load.loadPrice)

            // Expenses
            binding.tvLoadingCharge.text = CurrencyUtils.format(load.loadingCharge)
            binding.tvDieselCost.text = CurrencyUtils.format(load.dieselCost)
            binding.tvPoliceExpense.text = CurrencyUtils.format(load.policeExpense)
            binding.tvTollFee.text = CurrencyUtils.format(load.tollFee)
            binding.tvDriverCharge.text = CurrencyUtils.format(load.driverCharge)
            binding.tvUnloadingCharge.text = CurrencyUtils.format(load.unloadingCharge)
            binding.tvOtherExpense.text = CurrencyUtils.format(load.otherExpense)

            // Totals
            binding.tvTotalExpense.text = CurrencyUtils.format(load.totalExpense)

            binding.tvProfit.text = CurrencyUtils.format(load.profit)
            val profitColor = if (load.profit >= 0) {
                ContextCompat.getColor(this@LoadDetailActivity, R.color.profit_positive)
            } else {
                ContextCompat.getColor(this@LoadDetailActivity, R.color.profit_negative)
            }
            binding.tvProfit.setTextColor(profitColor)

            // Buttons
            binding.btnEdit.setOnClickListener {
                val intent = Intent(this@LoadDetailActivity, AddLoadActivity::class.java)
                intent.putExtra("edit_load_id", load.id)
                intent.putExtra("load_data", load)
                startActivity(intent)
                finish()
            }

            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(this@LoadDetailActivity)
                    .setTitle("Delete Load")
                    .setMessage("Are you sure you want to delete this load? This cannot be undone.")
                    .setPositiveButton("Delete") { _, _ ->
                        lifecycleScope.launch {
                            repo.deleteLoad(load)
                            Toast.makeText(this@LoadDetailActivity, "Deleted", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish(); return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}
