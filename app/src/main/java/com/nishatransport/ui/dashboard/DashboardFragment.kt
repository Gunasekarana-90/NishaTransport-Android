package com.nishatransport.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nishatransport.NishaTransportApp
import com.nishatransport.databinding.FragmentDashboardBinding
import com.nishatransport.ui.loads.add.AddLoadActivity
import com.nishatransport.ui.loads.history.LoadHistoryFragment
import com.nishatransport.utils.CurrencyUtils
import com.nishatransport.utils.DateUtils
import com.nishatransport.utils.ViewModelFactory

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels {
        ViewModelFactory((requireActivity().application as NishaTransportApp).repository)
    }

    private lateinit var recentLoadsAdapter: RecentLoadsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Refresh on resume
        viewModel.loadDashboardData()
    }

    private fun setupRecyclerView() {
        recentLoadsAdapter = RecentLoadsAdapter { load ->
            val intent = Intent(requireContext(),
                com.nishatransport.ui.loads.detail.LoadDetailActivity::class.java)
            intent.putExtra("load_id", load.id)
            startActivity(intent)
        }
        binding.rvRecentLoads.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvRecentLoads.adapter = recentLoadsAdapter
    }

    private fun setupObservers() {
        viewModel.todaySummary.observe(viewLifecycleOwner) { summary ->
            binding.tvTodayRevenue.text = CurrencyUtils.format(summary.revenue)
            binding.tvTodayProfit.text = CurrencyUtils.format(summary.profit)
        }

        viewModel.monthSummary.observe(viewLifecycleOwner) { summary ->
            binding.tvMonthRevenue.text = CurrencyUtils.format(summary.revenue)
            binding.tvMonthProfit.text = CurrencyUtils.format(summary.profit)
        }

        viewModel.yearProfit.observe(viewLifecycleOwner) { profit ->
            binding.tvYearProfit.text = CurrencyUtils.format(profit)
        }

        viewModel.totalLoadCount.observe(viewLifecycleOwner) { count ->
            binding.tvTotalLoads.text = count.toString()
        }

        viewModel.recentLoads.observe(viewLifecycleOwner) { loads ->
            recentLoadsAdapter.submitList(loads)
            binding.tvNoRecentLoads.visibility = if (loads.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.btnAddLoad.setOnClickListener {
            startActivity(Intent(requireContext(), AddLoadActivity::class.java))
        }

        binding.btnViewReports.setOnClickListener {
            // Navigate to Analytics tab
            (activity as? com.nishatransport.ui.MainActivity)?.navigateTo(2)
        }

        binding.btnExportData.setOnClickListener {
            // Navigate to Reports tab
            (activity as? com.nishatransport.ui.MainActivity)?.navigateTo(3)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDashboardData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
// Note: RecyclerView LayoutManager is set in setupRecyclerView via adapter
