package com.rarms.stocks.performance.ui.charts

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.rarms.stocks.performance.util.PercentageAxisValueFormatter

val CHART_COLORS = intArrayOf(
    ColorTemplate.MATERIAL_COLORS[0],
    ColorTemplate.MATERIAL_COLORS[1],
    ColorTemplate.MATERIAL_COLORS[2]
)

fun BarLineChartBase<*>.prepareChart() {
    this.setDrawGridBackground(true)
    this.description.isEnabled = true
    this.setDrawBorders(true)

    // set x values
    this.xAxis.setDrawAxisLine(true)
    this.xAxis.position = XAxis.XAxisPosition.BOTTOM
    this.xAxis.setDrawGridLines(true)
    this.xAxis.valueFormatter = DefaultAxisValueFormatter(3)

    // set y values
    this.axisLeft.isEnabled = true
    this.axisRight.isEnabled = false
    this.axisLeft.valueFormatter = when (this) {
        is LineChart -> PercentageAxisValueFormatter()
        else -> DefaultAxisValueFormatter(4)
    }
    this.axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
    this.axisLeft.spaceTop = 15f

    // enable touch gestures
    this.setTouchEnabled(true)

    // enable scaling and dragging
    this.isDragEnabled = true
    this.setScaleEnabled(true)

    // if disabled, scaling can be done on x- and y-axis separately
    this.setPinchZoom(false)

    val l = this.legend
    l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
    l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
    l.orientation = Legend.LegendOrientation.HORIZONTAL
    l.setDrawInside(false)
    l.form = Legend.LegendForm.SQUARE
    l.formSize = 9f
    l.textSize = 11f
    l.xEntrySpace = 4f
}
