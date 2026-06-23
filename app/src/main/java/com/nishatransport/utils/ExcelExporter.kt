package com.nishatransport.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import com.nishatransport.data.local.entity.Load
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

object ExcelExporter {

    fun generateExcel(
        context: Context,
        loads: List<Load>,
        period: String
    ): File? {
        return try {
            val fileName = "NishaTransport_${period.replace(" ", "_")}_${System.currentTimeMillis()}.xlsx"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            val workbook: Workbook = XSSFWorkbook()

            // ─── Summary Sheet ───
            createSummarySheet(workbook, loads, period)

            // ─── Details Sheet ───
            createDetailsSheet(workbook, loads)

            // Write file
            FileOutputStream(file).use { workbook.write(it) }
            workbook.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createSummarySheet(workbook: Workbook, loads: List<Load>, period: String) {
        val sheet = workbook.createSheet("Summary")
        sheet.setColumnWidth(0, 7000)
        sheet.setColumnWidth(1, 6000)

        val styles = createStyles(workbook)

        // Title
        val titleRow = sheet.createRow(0)
        titleRow.heightInPoints = 30f
        val titleCell = titleRow.createCell(0)
        titleCell.setCellValue("Nisha Transport - $period Report")
        titleCell.cellStyle = styles["title"]
        sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 1))

        // Subtitle
        val subRow = sheet.createRow(1)
        val subCell = subRow.createCell(0)
        subCell.setCellValue("Transport Business Management Report")
        subCell.cellStyle = styles["subtitle"]
        sheet.addMergedRegion(CellRangeAddress(1, 1, 0, 1))

        sheet.createRow(2) // spacer

        // Summary header
        val summaryHeader = sheet.createRow(3)
        val shCell = summaryHeader.createCell(0)
        shCell.setCellValue("Financial Summary")
        shCell.cellStyle = styles["sectionHeader"]
        sheet.addMergedRegion(CellRangeAddress(3, 3, 0, 1))

        val summaryData = listOf(
            Pair("Total Revenue (₹)", loads.sumOf { it.loadPrice }),
            Pair("Total Expense (₹)", loads.sumOf { it.totalExpense }),
            Pair("Net Profit (₹)", loads.sumOf { it.profit }),
            Pair("Total Loads", loads.size.toDouble()),
        )

        summaryData.forEachIndexed { i, (label, value) ->
            val row = sheet.createRow(4 + i)
            val lCell = row.createCell(0)
            lCell.setCellValue(label)
            lCell.cellStyle = styles["label"]
            val vCell = row.createCell(1)
            vCell.setCellValue(value)
            vCell.cellStyle = styles["value"]
        }

        sheet.createRow(9) // spacer

        // Expense breakdown
        val expHeader = sheet.createRow(10)
        val ehCell = expHeader.createCell(0)
        ehCell.setCellValue("Expense Breakdown")
        ehCell.cellStyle = styles["sectionHeader"]
        sheet.addMergedRegion(CellRangeAddress(10, 10, 0, 1))

        val expenses = listOf(
            Pair("Loading Charges (₹)", loads.sumOf { it.loadingCharge }),
            Pair("Diesel Cost (₹)", loads.sumOf { it.dieselCost }),
            Pair("Police Expenses (₹)", loads.sumOf { it.policeExpense }),
            Pair("Tollgate Fee (₹)", loads.sumOf { it.tollFee }),
            Pair("Driver Charge (₹)", loads.sumOf { it.driverCharge }),
            Pair("Unloading Cost (₹)", loads.sumOf { it.unloadingCharge }),
            Pair("Other Expenses (₹)", loads.sumOf { it.otherExpense })
        )

        expenses.forEachIndexed { i, (label, value) ->
            val row = sheet.createRow(11 + i)
            val lCell = row.createCell(0)
            lCell.setCellValue(label)
            lCell.cellStyle = styles["label"]
            val vCell = row.createCell(1)
            vCell.setCellValue(value)
            vCell.cellStyle = styles["value"]
        }
    }

    private fun createDetailsSheet(workbook: Workbook, loads: List<Load>) {
        val sheet = workbook.createSheet("Load Details")
        val styles = createStyles(workbook)

        // Set column widths
        val widths = intArrayOf(3500, 4500, 4500, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3500, 3500)
        widths.forEachIndexed { i, w -> sheet.setColumnWidth(i, w) }

        // Header row
        val headerRow = sheet.createRow(0)
        headerRow.heightInPoints = 20f
        val headers = listOf(
            "Date", "From", "To", "Revenue(₹)",
            "Loading(₹)", "Diesel(₹)", "Police(₹)", "Toll(₹)",
            "Driver(₹)", "Unloading(₹)", "Other(₹)", "Total Exp(₹)", "Profit(₹)"
        )
        headers.forEachIndexed { i, header ->
            val cell = headerRow.createCell(i)
            cell.setCellValue(header)
            cell.cellStyle = styles["tableHeader"]
        }

        // Data rows
        loads.forEachIndexed { rowIdx, load ->
            val row = sheet.createRow(rowIdx + 1)
            val isAlt = rowIdx % 2 == 0

            val cells = listOf(
                DateUtils.formatDate(load.date),
                load.fromLocation,
                load.toLocation,
                load.loadPrice,
                load.loadingCharge,
                load.dieselCost,
                load.policeExpense,
                load.tollFee,
                load.driverCharge,
                load.unloadingCharge,
                load.otherExpense,
                load.totalExpense,
                load.profit
            )

            cells.forEachIndexed { colIdx, value ->
                val cell = row.createCell(colIdx)
                when (value) {
                    is String -> {
                        cell.setCellValue(value)
                        cell.cellStyle = if (isAlt) styles["dataAlt"] else styles["data"]
                    }
                    is Double -> {
                        cell.setCellValue(value)
                        val style = when {
                            colIdx == 12 && value >= 0 -> if (isAlt) styles["profitAlt"] else styles["profit"]
                            colIdx == 12 -> if (isAlt) styles["lossAlt"] else styles["loss"]
                            else -> if (isAlt) styles["numAlt"] else styles["num"]
                        }
                        cell.cellStyle = style
                    }
                }
            }
        }

        // Totals row
        val totalRow = sheet.createRow(loads.size + 1)
        val totalLabelCell = totalRow.createCell(0)
        totalLabelCell.setCellValue("TOTALS")
        totalLabelCell.cellStyle = styles["tableHeader"]

        val totals = mapOf(
            3 to loads.sumOf { it.loadPrice },
            4 to loads.sumOf { it.loadingCharge },
            5 to loads.sumOf { it.dieselCost },
            6 to loads.sumOf { it.policeExpense },
            7 to loads.sumOf { it.tollFee },
            8 to loads.sumOf { it.driverCharge },
            9 to loads.sumOf { it.unloadingCharge },
            10 to loads.sumOf { it.otherExpense },
            11 to loads.sumOf { it.totalExpense },
            12 to loads.sumOf { it.profit }
        )

        totals.forEach { (col, value) ->
            val cell = totalRow.createCell(col)
            cell.setCellValue(value)
            cell.cellStyle = styles["totalValue"]
        }
    }

    private fun createStyles(workbook: Workbook): Map<String, CellStyle> {
        val styles = mutableMapOf<String, CellStyle>()
        val createHelper = workbook.creationHelper

        fun font(bold: Boolean = false, size: Short = 11, colorIndex: Short = IndexedColors.BLACK.index): Font {
            return workbook.createFont().apply {
                this.bold = bold
                fontHeightInPoints = size
                color = colorIndex
            }
        }

        // Title
        styles["title"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, size = 16))
            alignment = HorizontalAlignment.CENTER
            fillForegroundColor = IndexedColors.ROYAL_BLUE.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(font(bold = true, size = 16, colorIndex = IndexedColors.WHITE.index))
        }

        styles["subtitle"] = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            setFont(font(size = 11, colorIndex = IndexedColors.GREY_50_PERCENT.index))
        }

        styles["sectionHeader"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, size = 12, colorIndex = IndexedColors.WHITE.index))
            fillForegroundColor = IndexedColors.DARK_BLUE.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
        }

        styles["label"] = workbook.createCellStyle().apply {
            setFont(font(size = 11))
        }

        styles["value"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, size = 11))
            dataFormat = createHelper.createDataFormat().getFormat("#,##0.00")
        }

        styles["tableHeader"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, size = 10, colorIndex = IndexedColors.WHITE.index))
            fillForegroundColor = IndexedColors.ROYAL_BLUE.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            setBorderAll(BorderStyle.THIN)
        }

        fun dataStyle(alt: Boolean, numFmt: String = "@"): CellStyle = workbook.createCellStyle().apply {
            if (alt) {
                fillForegroundColor = IndexedColors.LIGHT_CORNFLOWER_BLUE.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
            }
            setBorderAll(BorderStyle.HAIR)
            if (numFmt != "@") dataFormat = createHelper.createDataFormat().getFormat(numFmt)
        }

        styles["data"] = dataStyle(false)
        styles["dataAlt"] = dataStyle(true)
        styles["num"] = dataStyle(false, "#,##0.00")
        styles["numAlt"] = dataStyle(true, "#,##0.00")

        styles["profit"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, colorIndex = IndexedColors.GREEN.index))
            dataFormat = createHelper.createDataFormat().getFormat("#,##0.00")
            setBorderAll(BorderStyle.HAIR)
        }
        styles["profitAlt"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, colorIndex = IndexedColors.GREEN.index))
            fillForegroundColor = IndexedColors.LIGHT_CORNFLOWER_BLUE.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            dataFormat = createHelper.createDataFormat().getFormat("#,##0.00")
            setBorderAll(BorderStyle.HAIR)
        }
        styles["loss"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, colorIndex = IndexedColors.RED.index))
            dataFormat = createHelper.createDataFormat().getFormat("#,##0.00")
            setBorderAll(BorderStyle.HAIR)
        }
        styles["lossAlt"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, colorIndex = IndexedColors.RED.index))
            fillForegroundColor = IndexedColors.LIGHT_CORNFLOWER_BLUE.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            dataFormat = createHelper.createDataFormat().getFormat("#,##0.00")
            setBorderAll(BorderStyle.HAIR)
        }
        styles["totalValue"] = workbook.createCellStyle().apply {
            setFont(font(bold = true, size = 11))
            fillForegroundColor = IndexedColors.LIGHT_YELLOW.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            dataFormat = createHelper.createDataFormat().getFormat("#,##0.00")
            setBorderAll(BorderStyle.THIN)
        }

        return styles
    }

    private fun CellStyle.setBorderAll(style: BorderStyle) {
        borderTop = style; borderBottom = style
        borderLeft = style; borderRight = style
    }

    fun shareExcel(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Nisha Transport Data Export")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Excel File"))
    }
}
