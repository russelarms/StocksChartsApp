package com.rarms.stocks.performance.ui.charts.candlesticks

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.rarms.stocks.performance.model.QuoteSymbol
import com.rarms.stocks.performance.model.Timeframe
import com.rarms.stocks.performance.ui.charts.CHART_COLORS
import com.rarms.stocks.performance.ui.charts.prepareChart

@Composable
fun CandlesticksChartBody(
    data: List<QuoteSymbol>,
    timeframe: Timeframe,
    selectedStockIndex: Int
) {
    AndroidView(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxSize(),
        factory = { context ->
            createCandleSticksChart(context, data, selectedStockIndex)
        },
        update = { chart ->
            chart.data = prepareCandlesticksData(data, selectedStockIndex)
            chart.description.text = "${timeframe.name}ly timeframe"
            (chart as CandleStickChart).updateTimeframe(data, timeframe)
            chart.invalidate()
        }
    )
}

fun createCandleSticksChart(
    context: Context,
    data: List<QuoteSymbol>,
    selectedStockIndex: Int,
): BarLineChartBase<CandleData> {
    val chart = CandleStickChart(context)
    chart.prepareChart()
    chart.data = prepareCandlesticksData(data, selectedStockIndex)
    return chart
}

private fun CandleStickChart.updateTimeframe(data: List<QuoteSymbol>, timeframe: Timeframe) {
    //todo change between stocks
    val timestamps = data[0].timestamps
    val xAxisFormatter: ValueFormatter = DefaultAxisValueFormatter(3)
    //todo try to apply correct timestamps
    //        if (timeframe == Timeframe.Week) CandlestickWeekAxisValueFormatter(timestamps)
    //        else CandlestickMonthAxisValueFormatter(timestamps)
    this.xAxis.valueFormatter = xAxisFormatter
}

private fun prepareCandlesticksData(
    data: List<QuoteSymbol>,
    selectedStockIndex: Int
): CandleData {
    val quoteSymbol = data[selectedStockIndex]
    val candleEntries: List<CandleEntry> =
        quoteSymbol.timestamps.mapIndexed { index, _ ->
            val high = quoteSymbol.highs[index]
            val low = quoteSymbol.lows[index]
            val open = quoteSymbol.opens[index]
            val close = quoteSymbol.closures[index]
            CandleEntry(
                index.toFloat(),
                high.toFloat(), low.toFloat(),
                open.toFloat(), close.toFloat()
            )
        }
    val dataSet = CandleDataSet(candleEntries, quoteSymbol.symbol)
    val color: Int = CHART_COLORS[selectedStockIndex % CHART_COLORS.size]
    dataSet.color = color
    dataSet.setDrawIcons(false)
    dataSet.axisDependency = YAxis.AxisDependency.LEFT
    dataSet.shadowColor = Color.DKGRAY
    dataSet.shadowWidth = 0.7f
    dataSet.decreasingColor = CHART_COLORS[2]
    dataSet.decreasingPaintStyle = Paint.Style.FILL
    dataSet.increasingColor = CHART_COLORS[0]
    dataSet.increasingPaintStyle = Paint.Style.FILL
    dataSet.neutralColor = Color.BLUE
    return CandleData(dataSet)
}
