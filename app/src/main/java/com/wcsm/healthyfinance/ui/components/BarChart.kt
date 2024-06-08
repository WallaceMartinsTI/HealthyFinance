package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun BarChartScreen(
    income: Float,
    expense: Float,
    investment: Float
) {
    val entries = listOf(
        BarEntry(0f, income, "Receita"),
        BarEntry(1f, expense, "Gastos"),
        BarEntry(2f, investment, "Investimento")
    )

    val colors = listOf(
        android.graphics.Color.parseColor("#3FAA3C"), // INCOME
        android.graphics.Color.parseColor("#E73838"), // EXPENSE
        android.graphics.Color.parseColor("#FF8514")  // INVESTMENT
    )

    var barChart: BarChart? by remember { mutableStateOf(null) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.size(width = 350.dp, height = 150.dp),
            factory = { context ->
                BarChart(context).apply {
                    val dataSet = BarDataSet(entries, "").apply {
                        setColors(colors)
                        valueTextSize = 12f
                        valueTextColor = android.graphics.Color.WHITE
                        valueFormatter = PercentageValueFormatter()
                    }

                    val barData = BarData(dataSet)
                    data = barData
                    description.isEnabled = false

                    xAxis.setDrawGridLines(false)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.granularity = 1f
                    xAxis.isGranularityEnabled = true
                    xAxis.isEnabled = false

                    axisLeft.setDrawGridLines(false)
                    axisLeft.isEnabled = false

                    axisRight.isEnabled = false

                    legend.isEnabled = false

                    barChart = this
                }
            },
            update = { chart ->
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        )

        LaunchedEffect(income, expense, investment) {
            barChart?.data?.apply {
                val dataSet = getDataSetByIndex(0) as BarDataSet
                dataSet.values = entries
                notifyDataChanged()
            }
            barChart?.notifyDataSetChanged()
            barChart?.invalidate()
        }

        barChart?.let {
            CustomLegend(chart = it)
        }
    }
}

@Composable
fun CustomLegend(chart: BarChart) {
    val dataSet = chart.data.dataSets[0]
    val colors = dataSet.colors

    Row(
        modifier = Modifier.width(350.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until dataSet.entryCount) {
            val entry = dataSet.getEntryForIndex(i)
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(colors[i]))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entry.data as String,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

class PercentageValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return String.format("%.1f%%", value)
    }
}

@Preview
@Composable
fun BarChartScreenPreview() {
    BarChartScreen(income = 50.0f, expense = 30.0f, investment = 20.0f)
}