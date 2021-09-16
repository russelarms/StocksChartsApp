package com.rarms.stocks.performance.ui.charts.performance

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.rarms.stocks.performance.model.PerformanceBy
import com.rarms.stocks.performance.model.QuoteSymbol
import com.rarms.stocks.performance.model.Timeframe
import com.rarms.stocks.performance.ui.charts.CHART_COLORS
import com.rarms.stocks.performance.ui.charts.prepareChart
import com.rarms.stocks.performance.util.MonthAxisValueFormatter
import com.rarms.stocks.performance.util.WeekAxisValueFormatter
import com.rarms.stocks.performance.util.calculatePerformance
import java.util.*

@Composable
fun PerformanceChartBody(
    data: List<QuoteSymbol>,
    timeframe: Timeframe,
    performanceBy: PerformanceBy
) {
    AndroidView(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxSize(),
        factory = { context ->
            createLineChart(context, data, performanceBy)
        },
        update = { chart ->
            chart.data = prepareLineData(data, performanceBy)
            chart.description.text = "${timeframe.name}ly performance comparison"
            (chart as LineChart).updateTimeframe(timeframe)
            chart.invalidate()
        }
    )
}

fun createLineChart(
    context: Context,
    data: List<QuoteSymbol>,
    performanceBy: PerformanceBy,
): BarLineChartBase<LineData> {
    val chart = LineChart(context)
    chart.prepareChart()
    chart.data = prepareLineData(data, performanceBy)
    return chart
}

private fun LineChart.updateTimeframe(timeframe: Timeframe) {
    val xAxisFormatter: ValueFormatter =
        if (timeframe == Timeframe.Week) WeekAxisValueFormatter() else MonthAxisValueFormatter()
    this.xAxis.valueFormatter = xAxisFormatter
}

private fun prepareLineData(data: List<QuoteSymbol>, performanceBy: PerformanceBy): LineData {
    val dataSets = ArrayList<ILineDataSet>()
    data
        .map { item ->
            val timestamps = item.timestamps
            val collection = if (performanceBy == PerformanceBy.Opens) item.opens else item.closures
            val performances = calculatePerformance(collection)
            val list = timestamps zip performances
            val entryList = list
                .sortedBy { it.first }
                .map {
                    Entry((it.first * 1000).toFloat(), it.second.toFloat())
                }
            Pair(entryList, item.symbol)
        }
        .mapIndexed { index: Int, pair: Pair<List<Entry>, String> ->
            val dataSet = LineDataSet(pair.first, pair.second)
            dataSet.lineWidth = 2.5f
            dataSet.circleRadius = 4f
            val color: Int = CHART_COLORS.get(index % CHART_COLORS.size)
            dataSet.color = color
            dataSet.setCircleColor(color)
            dataSets.add(dataSet)
        }
    return LineData(dataSets)
}