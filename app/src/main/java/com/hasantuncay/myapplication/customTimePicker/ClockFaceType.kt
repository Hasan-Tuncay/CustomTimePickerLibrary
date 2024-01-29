package com.hasantuncay.myapplication.customTimePicker

import java.util.Objects
sealed class ClockFaceType {
    object Hour : ClockFaceType() {
        val hours: List<String> = (1..12).map { it.toString() }
    }

    object Minute : ClockFaceType() {
        val minutes: List<String> = (1..60).map { it.toString() }
    }
}

