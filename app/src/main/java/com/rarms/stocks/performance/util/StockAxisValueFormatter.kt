package com.rarms.stocks.performance.util

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class WeekAxisValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val timeLong = value.toLong()
        val simpleDateFormat = SimpleDateFormat("dd'T'HH:mm", Locale.getDefault())
        return simpleDateFormat.format(Date(timeLong))
    }
}

class MonthAxisValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val timeLong = value.toLong()
        val simpleDateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        return simpleDateFormat.format(Date(timeLong))
    }
}

//fixme
class CandlestickWeekAxisValueFormatter(private val timestamps: List<Long> = emptyList()) :
    ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        val timeLong = timestamps[index]
        val simpleDateFormat = SimpleDateFormat("dd'T'HH:mm", Locale.getDefault())
        return simpleDateFormat.format(Date(timeLong))
    }
}

//fixme
class CandlestickMonthAxisValueFormatter(private val timestamps: List<Long> = emptyList()) :
    ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        val timeLong = timestamps[index]
        val simpleDateFormat = SimpleDateFormat("dd'T'HH:mm", Locale.getDefault())
        return simpleDateFormat.format(Date(timeLong))
    }
}


class PercentageAxisValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val volume = value.toInt()
        return "$volume%"
    }
}