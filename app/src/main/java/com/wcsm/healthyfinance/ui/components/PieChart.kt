package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.wcsm.healthyfinance.data.model.CategoryAndTotal
import com.wcsm.healthyfinance.data.model.HistoryItemType
import com.wcsm.healthyfinance.ui.util.ValueAndPercentageFormatter
import kotlin.math.ceil

@Composable
fun PieChartScreen(
    type: HistoryItemType,
    chartChanged: Boolean,
    categoryTotals: List<CategoryAndTotal>,
    selectedDate: String
) {
    val entries = categoryTotals.map { categoryTotal ->
        PieEntry(categoryTotal.total.toFloat(), categoryTotal.name)
    }

    val colors = listOf(
        android.graphics.Color.parseColor("#2077b4"),
        android.graphics.Color.parseColor("#FF7E0E"),
        android.graphics.Color.parseColor("#2AA02D"),
        android.graphics.Color.parseColor("#D52628"),
        android.graphics.Color.parseColor("#9566BC"),
        android.graphics.Color.parseColor("#8C564B"),
        android.graphics.Color.parseColor("#E377C2"),
        android.graphics.Color.parseColor("#4f4949"),
        android.graphics.Color.parseColor("#DCE21D"),
    )

    var pieChart: PieChart? by remember { mutableStateOf(null) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.size(width = 350.dp, height = 200.dp),
            factory = { context ->
                PieChart(context).apply {
                    val dataSet = PieDataSet(entries, "").apply {
                        setColors(colors)
                        valueTextSize = 12f
                        valueTextColor = android.graphics.Color.WHITE
                        valueFormatter = ValueAndPercentageFormatter()
                    }

                    val pieData = PieData(dataSet)
                    data = pieData
                    description.isEnabled = false
                    legend.isEnabled = false
                    setEntryLabelColor(android.graphics.Color.WHITE)
                    setDrawEntryLabels(false)

                    isDrawHoleEnabled = false

                    pieChart = this
                }
            },
            update = { pieChart ->
                pieChart.notifyDataSetChanged()
                pieChart.invalidate()
            }
        )

        LaunchedEffect(type, chartChanged, selectedDate) {
            pieChart?.data?.apply {
                val dataSet = getDataSetByIndex(0) as PieDataSet
                dataSet.values = entries
                notifyDataChanged()
            }
            pieChart?.notifyDataSetChanged()
            pieChart?.invalidate()
        }

        Spacer(modifier = Modifier.height(8.dp))

        pieChart?.let {
            CustomLegend(chart = it)
        }
    }
}


@Composable
fun CustomLegend(chart: PieChart) {
    val dataSet = chart.data.dataSets[0]
    val colors = dataSet.colors
    val numRows = ceil(dataSet.entryCount / 3f).toInt()

    Column(
        modifier = Modifier
            .width(350.dp)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until numRows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in row * 3 until minOf((row + 1) * 3, dataSet.entryCount)) {
                    val entry = dataSet.getEntryForIndex(i)
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(colors[i]))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = entry.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}