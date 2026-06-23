package com.nishatransport.ui.loads.history

import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nishatransport.R
import com.nishatransport.data.local.entity.Load
import com.nishatransport.utils.CurrencyUtils
import com.nishatransport.utils.DateUtils

class LoadsAdapter(
    private val onItemClick: (Load) -> Unit,
    private val onEditClick: (Load) -> Unit,
    private val onDeleteClick: (Load) -> Unit
) : ListAdapter<Load, LoadsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_load, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvRoute: TextView = itemView.findViewById(R.id.tvRoute)
        private val tvRevenue: TextView = itemView.findViewById(R.id.tvRevenue)
        private val tvExpense: TextView = itemView.findViewById(R.id.tvExpense)
        private val tvProfit: TextView = itemView.findViewById(R.id.tvProfit)
        private val tvVehicle: TextView = itemView.findViewById(R.id.tvVehicle)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(load: Load) {
            tvDate.text = DateUtils.formatDate(load.date)
            tvRoute.text = "${load.fromLocation} → ${load.toLocation}"
            tvRevenue.text = CurrencyUtils.format(load.loadPrice)
            tvExpense.text = CurrencyUtils.format(load.totalExpense)
            tvProfit.text = CurrencyUtils.format(load.profit)

            if (load.vehicleNumber.isNotBlank()) {
                tvVehicle.visibility = View.VISIBLE
                tvVehicle.text = "🚛 ${load.vehicleNumber}"
            } else {
                tvVehicle.visibility = View.GONE
            }

            val profitColor = if (load.profit >= 0) {
                ContextCompat.getColor(itemView.context, R.color.profit_positive)
            } else {
                ContextCompat.getColor(itemView.context, R.color.profit_negative)
            }
            tvProfit.setTextColor(profitColor)

            itemView.setOnClickListener { onItemClick(load) }
            btnEdit.setOnClickListener { onEditClick(load) }
            btnDelete.setOnClickListener { onDeleteClick(load) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Load>() {
            override fun areItemsTheSame(oldItem: Load, newItem: Load) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Load, newItem: Load) = oldItem == newItem
        }
    }
}
