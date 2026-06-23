package com.nishatransport.ui.analytics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.nishatransport.NishaTransportApp
import com.nishatransport.R
import com.nishatransport.databinding.FragmentAnalyticsBinding
import com.nishatransport.utils.CurrencyUtils
import com.nishatransport.utils.DateUtils
import com.nishatransport.utils.ViewModelFactory
import java.util.Calendar

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnalyticsViewModel by viewModels {
        ViewModelFactory((requireActivity().application as NishaTransportApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMonthYearSelectors()
        setupObservers()
        setupCharts()
    }

    private fun setupMonthYearSelectors() {
        val months = arrayOf("January","February","March","April","May","June",
            "July","August","September","October","November","December")
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonth.adapter = monthAdapter
        binding.spinnerMonth.setSelection(viewModel.selectedMonth - 1)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (2020..currentYear + 1).map { it.toString() }.toTypedArray()
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerYear.adapter = yearAdapter
        binding.spinnerYear.setSelection(years.indexOf(viewModel.selectedYear.toString()))

        binding.btnApplyFilter.setOnClickListener {
            val month = binding.spinnerMonth.selectedItemPosition + 1
            val year = years[binding.spinnerYear.selectedItemPosition].toInt()
            viewModel.selectMonth(year, month)
            viewModel.selectYear(year)
            viewModel.selectedYear = year
            viewModel.selectedMonth = month
        }
    }

    private fun setupObservers() {
        viewModel.monthSummary.observe(viewLifecycleOwner) { summary ->
            binding.tvMonthRevenue.text = CurrencyUtils.format(summary.revenue)
            binding.tvMonthExpense.text = CurrencyUtils.format(summary.expense)
            binding.tvMonthProfit.text = CurrencyUtils.format(summary.profit)
            binding.tvMonthLoads.text = "${summary.loadCount} Loads"
        }

        viewModel.yearSummary.observe(viewLifecycleOwner) { summary ->
            binding.tvYearRevenue.text = CurrencyUtils.format(summary.revenue)
            binding.tvYearExpense.text = CurrencyUtils.format(summary.expense)
            binding.tvYearProfit.text = CurrencyUtils.format(summary.profit)
            binding.tvYearLoads.text = "${summary.loadCount} Loads"
        }

        viewModel.monthlyTrend.observe(viewLifecycleOwner) { trend ->
            updateCharts(trend)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun setupCharts() {
        // Revenue Line Chart
        listOf(binding.chartRevenue, binding.chartExpense, binding.chartProfit).forEach { chart ->
            chart.description.isEnabled = false
            chart.legend.isEnabled = true
            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.setScaleEnabled(true)
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.xAxis.granularity = 1f
            chart.axisRight.isEnabled = false
            chart.animateX(800)
        }

        // Bar Chart
        binding.chartComparison.description.isEnabled = false
        binding.chartComparison.setTouchEnabled(true)
        binding.chartComparison.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartComparison.axisRight.isEnabled = false
        binding.chartComparison.animateY(800)
    }

    private fun updateCharts(trend: List<com.nishatransport.data.local.dao.MonthlyTrendData>) {
        if (trend.isEmpty()) return

        val labels = trend.map { DateUtils.shortMonthName(it.month) }

        val colorRevenue = requireContext().getColor(R.color.chart_revenue)
        val colorExpense = requireContext().getColor(R.color.chart_expense)
        val colorProfit = requireContext().getColor(R.color.chart_profit)

        // Revenue Line Chart
        val revenueEntries = trend.mapIndexed { i, d -> Entry(i.toFloat(), d.revenue.toFloat()) }
        val revenueDataSet = LineDataSet(revenueEntries, "Revenue").apply {
            color = colorRevenue
            valueTextColor = Color.GRAY
            lineWidth = 2.5f
            circleRadius = 4f
            setCircleColor(colorRevenue)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = colorRevenue
            fillAlpha = 30
        }
        binding.chartRevenue.data = LineData(revenueDataSet)
        binding.chartRevenue.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.chartRevenue.invalidate()

        // Expense Line Chart
        val expenseEntries = trend.mapIndexed { i, d -> Entry(i.toFloat(), d.expense.toFloat()) }
        val expenseDataSet = LineDataSet(expenseEntries, "Expense").apply {
            color = colorExpense
            valueTextColor = Color.GRAY
            lineWidth = 2.5f
            circleRadius = 4f
            setCircleColor(colorExpense)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = colorExpense
            fillAlpha = 30
        }
        binding.chartExpense.data = LineData(expenseDataSet)
        binding.chartExpense.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.chartExpense.invalidate()

        // Profit Line Chart
        val profitEntries = trend.mapIndexed { i, d -> Entry(i.toFloat(), d.profit.toFloat()) }
        val profitDataSet = LineDataSet(profitEntries, "Profit").apply {
            color = colorProfit
            valueTextColor = Color.GRAY
            lineWidth = 2.5f
            circleRadius = 4f
            setCircleColor(colorProfit)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = colorProfit
            fillAlpha = 30
        }
        binding.chartProfit.data = LineData(profitDataSet)
        binding.chartProfit.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.chartProfit.invalidate()

        // Grouped Bar Chart - Revenue vs Expense vs Profit
        val revenueBar = trend.mapIndexed { i, d -> BarEntry(i.toFloat(), d.revenue.toFloat()) }
        val expenseBar = trend.mapIndexed { i, d -> BarEntry(i.toFloat(), d.expense.toFloat()) }
        val profitBar = trend.mapIndexed { i, d -> BarEntry(i.toFloat(), d.profit.toFloat()) }

        val barRevenue = BarDataSet(revenueBar, "Revenue").apply { color = colorRevenue }
        val barExpense = BarDataSet(expenseBar, "Expense").apply { color = colorExpense }
        val barProfit = BarDataSet(profitBar, "Profit").apply { color = colorProfit }

        val barData = BarData(barRevenue, barExpense, barProfit)
        val groupSpace = 0.3f
        val barSpace = 0.02f
        val barWidth = 0.2f
        barData.barWidth = barWidth

        binding.chartComparison.data = barData
        binding.chartComparison.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.chartComparison.xAxis.setCenterAxisLabels(true)
        binding.chartComparison.xAxis.axisMinimum = 0f
        binding.chartComparison.xAxis.axisMaximum = trend.size.toFloat()
        binding.chartComparison.groupBars(0f, groupSpace, barSpace)
        binding.chartComparison.invalidate()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAnalytics()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
