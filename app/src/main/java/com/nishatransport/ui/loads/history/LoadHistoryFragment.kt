package com.nishatransport.ui.loads.history

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.nishatransport.NishaTransportApp
import com.nishatransport.R
import com.nishatransport.data.local.entity.Load
import com.nishatransport.databinding.FragmentLoadHistoryBinding
import com.nishatransport.ui.loads.add.AddLoadActivity
import com.nishatransport.ui.loads.detail.LoadDetailActivity
import com.nishatransport.utils.DateUtils
import com.nishatransport.utils.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar

class LoadHistoryFragment : Fragment() {

    private var _binding: FragmentLoadHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoadHistoryViewModel by viewModels {
        ViewModelFactory((requireActivity().application as NishaTransportApp).repository)
    }

    private lateinit var loadsAdapter: LoadsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupFilterChips()
        setupObservers()
        setupFab()
    }

    private fun setupRecyclerView() {
        loadsAdapter = LoadsAdapter(
            onItemClick = { load ->
                val intent = Intent(requireContext(), LoadDetailActivity::class.java)
                intent.putExtra("load_id", load.id)
                startActivity(intent)
            },
            onEditClick = { load ->
                val intent = Intent(requireContext(), AddLoadActivity::class.java)
                intent.putExtra("edit_load_id", load.id)
                intent.putExtra("load_data", load)
                startActivity(intent)
            },
            onDeleteClick = { load ->
                showDeleteDialog(load)
            }
        )
        binding.rvLoads.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvLoads.adapter = loadsAdapter
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                return true
            }
        })
    }

    private fun setupFilterChips() {
        binding.chipAll.setOnClickListener { viewModel.clearFilter() }

        binding.chipThisMonth.setOnClickListener {
            val cal = Calendar.getInstance()
            viewModel.filterByMonth(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1
            )
        }

        binding.chipThisYear.setOnClickListener {
            viewModel.filterByYear(Calendar.getInstance().get(Calendar.YEAR))
        }

        binding.chipPickMonth.setOnClickListener {
            showMonthYearPicker()
        }
    }

    private fun showMonthYearPicker() {
        val months = arrayOf("January","February","March","April","May","June",
            "July","August","September","October","November","December")
        val cal = Calendar.getInstance()
        var selectedMonth = cal.get(Calendar.MONTH)
        var selectedYear = cal.get(Calendar.YEAR)

        AlertDialog.Builder(requireContext())
            .setTitle("Select Month & Year")
            .setItems(months) { _, which ->
                selectedMonth = which
                // Show year picker
                val years = (2020..selectedYear + 1).map { it.toString() }.toTypedArray()
                val currentYearIdx = years.indexOf(selectedYear.toString())
                AlertDialog.Builder(requireContext())
                    .setTitle("Select Year")
                    .setItems(years) { _, yearIdx ->
                        viewModel.filterByMonth(years[yearIdx].toInt(), selectedMonth + 1)
                    }
                    .show()
            }
            .show()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loads.collect { loads ->
                loadsAdapter.submitList(loads)
                binding.tvEmptyState.visibility = if (loads.isEmpty()) View.VISIBLE else View.GONE
                binding.tvLoadCount.text = "${loads.size} loads"
            }
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Load deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to delete load", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupFab() {
        binding.fabAddLoad.setOnClickListener {
            startActivity(Intent(requireContext(), AddLoadActivity::class.java))
        }
    }

    private fun showDeleteDialog(load: Load) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Load")
            .setMessage("Delete load from ${load.fromLocation} to ${load.toLocation} on ${DateUtils.formatDate(load.date)}?\n\nThis cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteLoad(load)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
