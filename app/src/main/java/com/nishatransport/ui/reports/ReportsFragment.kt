package com.nishatransport.ui.reports

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nishatransport.NishaTransportApp
import com.nishatransport.databinding.FragmentReportsBinding
import com.nishatransport.utils.DateUtils
import com.nishatransport.utils.ExcelExporter
import com.nishatransport.utils.PdfExporter
import com.nishatransport.utils.ViewModelFactory
import java.util.Calendar

class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportsViewModel by viewModels {
        ViewModelFactory((requireActivity().application as NishaTransportApp).repository)
    }

    private var reportStartDate = 0L
    private var reportEndDate = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupReportTypeSelector()
        setupExportButtons()
        setupObservers()
    }

    private fun setupReportTypeSelector() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val months = arrayOf("January","February","March","April","May","June",
            "July","August","September","October","November","December")
        val years = (2020..currentYear + 1).map { it.toString() }.toTypedArray()

        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonth.adapter = monthAdapter
        binding.spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerYear.adapter = yearAdapter
        binding.spinnerYear.setSelection(years.indexOf(currentYear.toString()))

        // Date range pickers
        binding.tvStartDate.setOnClickListener { pickDate(isStart = true) }
        binding.tvEndDate.setOnClickListener { pickDate(isStart = false) }

        // Report type radio buttons
        binding.rgReportType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbMonthly.id -> {
                    binding.layoutMonthYear.visibility = View.VISIBLE
                    binding.layoutDateRange.visibility = View.GONE
                }
                binding.rbYearly.id -> {
                    binding.layoutMonthYear.visibility = View.GONE
                    binding.layoutDateRange.visibility = View.GONE
                }
                binding.rbDateRange.id -> {
                    binding.layoutMonthYear.visibility = View.GONE
                    binding.layoutDateRange.visibility = View.VISIBLE
                }
                binding.rbAll.id -> {
                    binding.layoutMonthYear.visibility = View.GONE
                    binding.layoutDateRange.visibility = View.GONE
                }
            }
        }
        binding.rbMonthly.isChecked = true
    }

    private fun pickDate(isStart: Boolean) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                cal.set(year, month, dayOfMonth)
                if (isStart) {
                    reportStartDate = DateUtils.startOfDay(cal.timeInMillis)
                    binding.tvStartDate.text = DateUtils.formatDate(cal.timeInMillis)
                } else {
                    reportEndDate = DateUtils.endOfDay(cal.timeInMillis)
                    binding.tvEndDate.text = DateUtils.formatDate(cal.timeInMillis)
                }
            },
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setupExportButtons() {
        binding.btnExportPdf.setOnClickListener {
            generateAndExport(isPdf = true)
        }
        binding.btnExportExcel.setOnClickListener {
            generateAndExport(isPdf = false)
        }
    }

    private fun generateAndExport(isPdf: Boolean) {
        val years = (2020..Calendar.getInstance().get(Calendar.YEAR) + 1).map { it.toString() }
        val selectedYear = years[binding.spinnerYear.selectedItemPosition].toInt()
        val selectedMonth = binding.spinnerMonth.selectedItemPosition + 1

        when {
            binding.rbMonthly.isChecked -> {
                val periodStr = "${DateUtils.monthName(selectedMonth)} $selectedYear"
                viewModel.loadForMonth(selectedYear, selectedMonth) { loads ->
                    exportData(loads, "Monthly Report - $periodStr", periodStr, isPdf)
                }
            }
            binding.rbYearly.isChecked -> {
                val periodStr = "Year $selectedYear"
                viewModel.loadForYear(selectedYear) { loads ->
                    exportData(loads, "Yearly Report - $periodStr", periodStr, isPdf)
                }
            }
            binding.rbDateRange.isChecked -> {
                if (reportStartDate == 0L || reportEndDate == 0L) {
                    Toast.makeText(requireContext(), "Please select start and end dates", Toast.LENGTH_SHORT).show()
                    return
                }
                val periodStr = "${DateUtils.formatDate(reportStartDate)} to ${DateUtils.formatDate(reportEndDate)}"
                viewModel.loadForDateRange(reportStartDate, reportEndDate) { loads ->
                    exportData(loads, "Report - $periodStr", periodStr, isPdf)
                }
            }
            binding.rbAll.isChecked -> {
                viewModel.loadAll { loads ->
                    exportData(loads, "Complete Report - All Time", "All Time", isPdf)
                }
            }
        }
    }

    private fun exportData(
        loads: List<com.nishatransport.data.local.entity.Load>,
        title: String,
        period: String,
        isPdf: Boolean
    ) {
        if (loads.isEmpty()) {
            Toast.makeText(requireContext(), "No data found for selected period", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressExport.visibility = View.VISIBLE

        if (isPdf) {
            val file = PdfExporter.generateReport(requireContext(), loads, title, period)
            binding.progressExport.visibility = View.GONE
            if (file != null) {
                PdfExporter.sharePdf(requireContext(), file)
            } else {
                Toast.makeText(requireContext(), "Failed to generate PDF", Toast.LENGTH_SHORT).show()
            }
        } else {
            val file = ExcelExporter.generateExcel(requireContext(), loads, period)
            binding.progressExport.visibility = View.GONE
            if (file != null) {
                ExcelExporter.shareExcel(requireContext(), file)
            } else {
                Toast.makeText(requireContext(), "Failed to generate Excel", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressExport.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
