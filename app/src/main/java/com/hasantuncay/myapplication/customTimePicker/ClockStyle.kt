package com.hasantuncay.myapplication.customTimePicker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ClockColors(
    var clockFace: Color = Color.White,
    var centerPointColor: Color = Color.Gray,
    var hourHandsColor: Color = Color.Blue,
    var minuteHandColor: Color = Color.Yellow,
    var handPointDotColor: Color = Color.Blue,
    var clockNumberColor: Color=Color.Gray,
    var handPointCircleColor: Color = Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f)
)

data class ClockShapes(
    var clockfaceRadius: Dp = 400.dp,
    var centerPointRadius: Dp = 3.dp,
    var handPointDotRadius: Dp = 5.dp,
    var handPointCircleRadius: Dp = 15.dp

)

data class ClockStyle(
    val colors: ClockColors = ClockColors(),
    val shapes: ClockShapes = ClockShapes()
)