package com.john.itoo.routinecheckks

class Utils {
    companion object {
        const val LIMIT = 40
        val frequencyColorMap = hashMapOf(
            "HR" to R.color.hourly_color,
            "D" to R.color.daily_color,
            "WK" to R.color.weekly_color,
            "MN" to R.color.monthly_color,
            "YR" to R.color.yearly_color
        )
        val abbreviations = arrayOf("HR", "D", "WK", "MN", "YR")
    }
}