package com.hasantuncay.myapplication.customTimePicker

data class TimeInfo(var hour: Int, var minute: Int, var amPm: AmPm)
data class TimeInfo24(var hour: String, var minute: String, var amPm: String, var timeFormatted : String)
