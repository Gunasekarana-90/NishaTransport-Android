package com.nishatransport.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nishatransport.R
import com.nishatransport.data.local.entity.Load
import com.nishatransport.utils.CurrencyUtils
import com.nishatransport.utils.DateUtils

class RecentLoadsAdapter(
    private val onItemClick: (Load) -> Unit
) : ListAdapter<Load, RecentLoadsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_load, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvRoute: TextView = itemView.findViewById(R.id.tvRoute)
        private val tvRevenue: TextView = itemView.findViewById(R.id.tvRevenue)
        private val tvProfit: TextView = itemView.findViewById(R.id.tvProfit)

        fun bind(load: Load) {
            tvDate.text = DateUtils.formatDate(load.date)
            tvRoute.text = "${load.fromLocation} → ${load.toLocation}"
            tvRevenue.text = CurrencyUtils.format(load.loadPrice)

            val profitText = CurrencyUtils.format(load.profit)
            tvProfit.text = profitText
            val profitColor = if (load.profit >= 0) {
                ContextCompat.getColor(itemView.context, R.color.profit_positive)
            } else {
                ContextCompat.getColor(itemView.context, R.color.profit_negative)
            }
            tvProfit.setTextColor(profitColor)

            itemView.setOnClickListener { onItemClick(load) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Load>() {
            override fun areItemsTheSame(oldItem: Load, newItem: Load) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Load, newItem: Load) = oldItem == newItem
        }
    }
}
