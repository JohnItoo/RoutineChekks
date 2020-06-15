package com.john.itoo.routinecheckks.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.room.TypeConverter
import com.john.itoo.routinecheckks.R
import com.john.itoo.routinecheckks.extensions.readableString
import java.util.*

interface TimeHandler {
    fun setTime(date: Date)
}

class TimeUtils {

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toTimeStamp(date: Date?): Long? {
        return date?.time
    }

    fun setDateTimeListeners(
        context: Context,
        view: View,
        textView: TextView,
        timeHandler: TimeHandler,
        date: Date
    ) {
        view.setOnClickListener {

            val cal = Calendar.getInstance()
            cal.time = date

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                cal.set(Calendar.SECOND, 0)
                var currentDate = cal.time
                timeHandler.setTime(currentDate)
                textView.text = currentDate.readableString()

            }
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        context,
                        R.style.CustomPickerTheme,
                        timeSetListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()


                }
            DatePickerDialog(
                context,
                R.style.CustomPickerTheme,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }
    }

    fun getTimeToUpdate(date: Date, frequency: Int): Date {
        date.time += getFrequencyDifferenceInMs(frequency)
        return date
    }

    /**
     *  if a is lastTime and b is frequency
     *  currentTime - a (gives lost time)
     *  ceil(]lostTime/frequency) gives how many alarms should have gone of in lost times
     *  add 1 to how many alarms should have gone off and multiply by lastTime, a;
     */
    fun predictNextAlarm(lastDate: Date, frequency: Int): Date {
        var currentDate = Date()
        var difference = currentDate.time - lastDate.time
        var frequencyMs = getFrequencyDifferenceInMs(frequency)
        val noOfCalledTimes = ((difference + frequencyMs - 1) / frequencyMs) + 1 // Ceiling Division +  add one to avoid immediate Trigger
        val actualDistanceAfterCalledTimes = noOfCalledTimes * getFrequencyDifferenceInMs(frequency)
        val resultTime = lastDate.time + actualDistanceAfterCalledTimes
        val resultDate = Date()
        resultDate.time = resultTime
        return resultDate
    }

    fun getFrequencyDifferenceInMs(frequency: Int): Long {
        var repeatInterval: Long = 0
        val base: Long = 1000 * 60 * 60
        when (frequency) {
            1 -> repeatInterval = base
            2 -> repeatInterval = base * 24
            3 -> repeatInterval = base * 24 * 7
            4 -> repeatInterval = base * 24 * 7 * 30
            5 -> repeatInterval = base * 24 * 7 * 30 * 365
        }
        return repeatInterval
    }
}