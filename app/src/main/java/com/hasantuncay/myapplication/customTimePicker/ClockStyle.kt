package com.hasantuncay.myapplication.customTimePicker

import android.text.style.BackgroundColorSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ClockColors(
    var genaralBackgrgroundColor:Color= generalBackground,
    var clockFaceColor: Color = clockFace,
    var centerPointColor: Color = centerPointcircColor,
    var hourHandsColor: Color = hourHandLineColor,
    var minuteHandColor: Color = minuteHandLineColor,
    var handPointDotColor: Color = hourHandPointerDotColor,
    var clockNumberColor: Color = clockNumbColor,
    var handPointCircleColor: Color = hourHandPointerCircleColor
)

data class ClockShapes(
    var clockfaceRadius: Dp = 350.dp,
    var centerPointRadius: Dp = 3.dp,
    var handPointDotRadius: Dp = 5.dp,
    var handPointCircleRadius: Dp = 25.dp

)

data class ClockStyle(
    val colors: ClockColors = ClockColors(),
    val shapes: ClockShapes = ClockShapes(),
    val header: HeaderColors = HeaderColors(),
    var body: BodyColors = BodyColors(),
    val bottom: BottomButtonColor = BottomButtonColor()

)

data class HeaderColors(
    var headerBackgroundColor: Color = Color.White,
    var headerSeperateDotColor: Color = headerSeparator,
    var headeUnselectedColor: Color = headerUnselected,
    var headerSelectedColor: Color = headerSelected,
    var headerAmPmFontColor:Color=headerAmPmFont

)

data class BodyColors(
    var bodyBackgroundColor: Color = bodyBackground,
    var amPmUnselectedColor: Color = amPmUnSelected,
    var amPmSelectedColor: Color = amPmSelected,
    var amPmFontColor: Color = amPmBodyFontColor,
)

data class BottomButtonColor(
    var bottomBackgroundColor: Color = bottomButtonBackground,
    var bottomButtonFontColor: Color = bottomButtonFont,
    var bottomSeparatorColor:Color= bottomSeperator
)
