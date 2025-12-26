package com.example.cultivate.utils

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import java.time.LocalTime
import java.util.Calendar

object pickTool {
    private val TAG="pickTool"

    fun showTimePicker(context: Context, pickPick:(LocalTime)->Unit)  {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        Log.d(TAG, "showTimePicker:$hour+$minute ")

        val timePicker= TimePickerDialog(context,
            {_,selectedHour,selectedMinute->
                val time = LocalTime.of(selectedHour, selectedMinute)
                pickPick(time)
            },hour,minute,true)

        timePicker.show()
    }
}