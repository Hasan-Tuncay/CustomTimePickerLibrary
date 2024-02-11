package com.hasantuncay.myapplication.customTimePicker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ClockColors(
    var clockFaceColor: Color= clockFace,
    var centerPointColor: Color =centerPointcircColor ,
    var hourHandsColor: Color = hourHandLineColor,
    var minuteHandColor: Color = minuteHandLineColor,
    var handPointDotColor: Color = hourHandPointerDotColor,
    var clockNumberColor: Color=clockNumbColor,
    var handPointCircleColor: Color = hourHandPointerCircleColor
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