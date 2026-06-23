package com.nishatransport.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Environment
import androidx.core.content.FileProvider
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.nishatransport.data.local.entity.Load
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfExporter {

    private val BRAND_COLOR = BaseColor(41, 98, 255)    // Blue
    private val HEADER_COLOR = BaseColor(33, 37, 41)     // Dark
    private val PROFIT_GREEN = BaseColor(40, 167, 69)
    private val EXPENSE_RED = BaseColor(220, 53, 69)
    private val GRAY = BaseColor(108, 117, 125)
    private val LIGHT_GRAY = BaseColor(248, 249, 250)
    private val BORDER_GRAY = BaseColor(222, 226, 230)

    fun generateReport(
        context: Context,
        loads: List<Load>,
        title: String,
        period: String
    ): File? {
        return try {
            val fileName = "NishaTransport_Report_${System.currentTimeMillis()}.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            val document = Document(PageSize.A4, 36f, 36f, 54f, 36f)
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // ─── Header ───
            addHeader(document, title, period)

            // ─── Summary ───
            val totalRevenue = loads.sumOf { it.loadPrice }
            val totalExpense = loads.sumOf { it.totalExpense }
            val totalProfit = loads.sumOf { it.profit }
            addSummarySection(document, totalRevenue, totalExpense, totalProfit, loads.size)

            // ─── Expense Breakdown ───
            addExpenseBreakdown(document, loads)

            // ─── Load Details Table ───
            addLoadsTable(document, loads)

            document.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun addHeader(document: Document, title: String, period: String) {
        // Business Name
        val businessFont = Font(Font.FontFamily.HELVETICA, 24f, Font.BOLD, BRAND_COLOR)
        val businessName = Paragraph("🚛 Nisha Transport", businessFont)
        businessName.alignment = Element.ALIGN_CENTER
        businessName.spacingAfter = 4f
        document.add(businessName)

        // Tagline
        val tagFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, GRAY)
        val tagline = Paragraph("Transport Business Management", tagFont)
        tagline.alignment = Element.ALIGN_CENTER
        tagline.spacingAfter = 4f
        document.add(tagline)

        // Line separator
        val line = Paragraph("─".repeat(80))
        line.alignment = Element.ALIGN_CENTER
        line.spacingAfter = 8f
        document.add(line)

        // Report Title
        val titleFont = Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD, HEADER_COLOR)
        val titlePara = Paragraph(title, titleFont)
        titlePara.alignment = Element.ALIGN_CENTER
        titlePara.spacingAfter = 4f
        document.add(titlePara)

        // Period
        val periodFont = Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, GRAY)
        val periodPara = Paragraph("Period: $period", periodFont)
        periodPara.alignment = Element.ALIGN_CENTER
        periodPara.spacingAfter = 4f
        document.add(periodPara)

        // Generated date
        val dateFont = Font(Font.FontFamily.HELVETICA, 9f, Font.ITALIC, GRAY)
        val datePara = Paragraph(
            "Generated: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}",
            dateFont
        )
        datePara.alignment = Element.ALIGN_CENTER
        datePara.spacingAfter = 16f
        document.add(datePara)
    }

    private fun addSummarySection(
        document: Document,
        revenue: Double, expense: Double, profit: Double, count: Int
    ) {
        val sectionFont = Font(Font.FontFamily.HELVETICA, 13f, Font.BOLD, HEADER_COLOR)
        val sectionTitle = Paragraph("📊 Financial Summary", sectionFont)
        sectionTitle.spacingBefore = 8f
        sectionTitle.spacingAfter = 8f
        document.add(sectionTitle)

        val table = PdfPTable(4)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1f, 1f, 1f, 1f))

        addSummaryCard(table, "Total Revenue", CurrencyUtils.format(revenue), BRAND_COLOR)
        addSummaryCard(table, "Total Expense", CurrencyUtils.format(expense), EXPENSE_RED)
        addSummaryCard(table, "Net Profit", CurrencyUtils.format(profit),
            if (profit >= 0) PROFIT_GREEN else EXPENSE_RED)
        addSummaryCard(table, "Total Loads", "$count", BaseColor(255, 152, 0))

        document.add(table)
    }

    private fun addSummaryCard(table: PdfPTable, label: String, value: String, color: BaseColor) {
        val cell = PdfPCell()
        cell.backgroundColor = LIGHT_GRAY
        cell.borderColor = BORDER_GRAY
        cell.setPadding(12f)

        val labelFont = Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, GRAY)
        val valueFont = Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, color)

        cell.addElement(Phrase(label, labelFont))
        cell.addElement(Phrase(value, valueFont))
        table.addCell(cell)
    }

    private fun addExpenseBreakdown(document: Document, loads: List<Load>) {
        val sectionFont = Font(Font.FontFamily.HELVETICA, 13f, Font.BOLD, HEADER_COLOR)
        val title = Paragraph("💰 Expense Breakdown", sectionFont)
        title.spacingBefore = 16f
        title.spacingAfter = 8f
        document.add(title)

        val table = PdfPTable(2)
        table.widthPercentage = 60f
        table.horizontalAlignment = Element.ALIGN_LEFT

        val expenses = mapOf(
            "Loading Charges" to loads.sumOf { it.loadingCharge },
            "Diesel Cost" to loads.sumOf { it.dieselCost },
            "Police Expenses" to loads.sumOf { it.policeExpense },
            "Tollgate Fee" to loads.sumOf { it.tollFee },
            "Driver Charge" to loads.sumOf { it.driverCharge },
            "Unloading Cost" to loads.sumOf { it.unloadingCharge },
            "Other Expenses" to loads.sumOf { it.otherExpense }
        )

        val headerFont = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.WHITE)
        val hCell1 = PdfPCell(Phrase("Expense Type", headerFont))
        val hCell2 = PdfPCell(Phrase("Amount", headerFont))
        listOf(hCell1, hCell2).forEach {
            it.backgroundColor = BRAND_COLOR
            it.setPadding(8f)
            it.borderColor = BRAND_COLOR
        }
        table.addCell(hCell1)
        table.addCell(hCell2)

        val labelFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, HEADER_COLOR)
        val valueFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, EXPENSE_RED)

        var alternate = false
        expenses.forEach { (label, amount) ->
            val c1 = PdfPCell(Phrase(label, labelFont))
            val c2 = PdfPCell(Phrase(CurrencyUtils.format(amount), valueFont))
            listOf(c1, c2).forEach {
                it.setPadding(7f)
                if (alternate) it.backgroundColor = LIGHT_GRAY
                it.borderColor = BORDER_GRAY
            }
            table.addCell(c1)
            table.addCell(c2)
            alternate = !alternate
        }

        document.add(table)
    }

    private fun addLoadsTable(document: Document, loads: List<Load>) {
        if (loads.isEmpty()) return

        val sectionFont = Font(Font.FontFamily.HELVETICA, 13f, Font.BOLD, HEADER_COLOR)
        val title = Paragraph("📋 Load Details", sectionFont)
        title.spacingBefore = 16f
        title.spacingAfter = 8f
        document.add(title)

        val table = PdfPTable(6)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1.2f, 1.5f, 1.5f, 1.2f, 1.2f, 1.2f))

        val headers = listOf("Date", "From", "To", "Revenue", "Expense", "Profit")
        val headerFont = Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, BaseColor.WHITE)
        headers.forEach { header ->
            val cell = PdfPCell(Phrase(header, headerFont))
            cell.backgroundColor = BRAND_COLOR
            cell.setPadding(7f)
            cell.borderColor = BRAND_COLOR
            table.addCell(cell)
        }

        val labelFont = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, HEADER_COLOR)

        loads.forEachIndexed { index, load ->
            val bg = if (index % 2 == 0) BaseColor.WHITE else LIGHT_GRAY
            val cells = listOf(
                DateUtils.formatDate(load.date),
                load.fromLocation,
                load.toLocation,
                CurrencyUtils.format(load.loadPrice),
                CurrencyUtils.format(load.totalExpense),
                CurrencyUtils.format(load.profit)
            )
            cells.forEachIndexed { ci, text ->
                val font = if (ci == 5) {
                    Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD,
                        if (load.profit >= 0) PROFIT_GREEN else EXPENSE_RED)
                } else labelFont
                val cell = PdfPCell(Phrase(text, font))
                cell.setPadding(6f)
                cell.backgroundColor = bg
                cell.borderColor = BORDER_GRAY
                table.addCell(cell)
            }
        }

        document.add(table)
    }

    fun sharePdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Nisha Transport Report")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share PDF Report"))
    }
}
