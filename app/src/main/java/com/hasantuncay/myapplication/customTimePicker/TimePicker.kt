package com.hasantuncay.myapplication.customTimePicker

import android.graphics.Color

import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
@Composable
fun TimePicker(clockStyle: ClockStyle = ClockStyle()){
    Column (
        Modifier
             .padding(horizontal = 50.dp).background(color = androidx.compose.ui.graphics.Color.LightGray).border(0.5.dp, color = clockStyle.header.headeUnselectedColor),
        horizontalAlignment = Alignment.CenterHorizontally,
verticalArrangement = Arrangement.Center
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .height(84.dp).background(clockStyle.header.headerBackgroundColor)) {

        }

Clock(clockStyle=clockStyle)
        Row(
            Modifier
                .fillMaxWidth()
                .height(84.dp).background(color = androidx.compose.ui.graphics.Color.Blue)) {

        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun Clock(clockStyle: ClockStyle = ClockStyle()) {
var clockStyle by remember {
    mutableStateOf(clockStyle)
}
    val radius = clockStyle.shapes.clockfaceRadius.value
    val radiusInsideComponent=radius-30f
    var hourOrMinuteState by remember {
        mutableStateOf<ClockFaceType>(ClockFaceType.Hour)
    }
    var hourHandAngle by remember {
        mutableStateOf(hourAndMinuteAngle(hourMinuteAmPm()).hourAngle-90)
    }
    Log.d("hourHandAngle", "Clock: $hourHandAngle")

    Canvas(modifier = Modifier

        .pointerInput(Unit) {
            detectDragGestures(

                onDrag = { change, _ ->
                    val centerX = (size.width / 2).toFloat()
                    val centerY = (size.height / 2).toFloat()
                    // Kullanıcının dokunma noktasına göre açıyı hesaplayın
                    hourHandAngle =
                        calculateAngle(centerX, centerY, change.position.x, change.position.y)
                    Log.d(
                        "Angle",
                        "Clock: angle ${
                            calculateAngle(
                                centerX,
                                centerY,
                                change.position.x,
                                change.position.y
                            )
                        }"
                    )
                },
                onDragEnd = {
                    Log.d(
                        "Angle",
                        "Clock: min end   ${hourOrMinuteState}"
                    )
                    when (hourOrMinuteState) {
                        ClockFaceType.Hour -> {
                            fromAngleToHour(hourHandAngle)
                            Log.d(
                                "Angle",
                                "Clock: hour end ${
                                    fromAngleToHour(hourHandAngle)
                                } "
                            )
                        }

                        else -> {
                            fromAngleToMinut(hourHandAngle)
                            Log.d(
                                "Angle",
                                "Clock: min end   ${fromAngleToMinut(hourHandAngle)}"
                            )
                        }
                    }


                    hourOrMinuteState = ClockFaceType.Minute

                }

            )
        }

    ) {

        val angleToRadyanForDraw = (hourHandAngle) * (PI / 180f).toFloat()

        ClockCircle(radius, clockStyle)

        ClockNumbers(clockStyle, hourOrMinuteState, radiusInsideComponent)

        ClockCenter(clockStyle)

        HourHand(radiusInsideComponent, clockStyle, angleToRadyanForDraw)

        smallAndLargeHandDotPointer(clockStyle, radiusInsideComponent, angleToRadyanForDraw)
    }


}




fun fromAngleToHour(angle: Float): Int {
    val normalizedAngle =   angle +90
    return when (normalizedAngle) {
        in 0f..29f -> 12
        in 30f..59f -> 1
        in 60f..89f -> 2
        in 90f..119f -> 3
        in 120f..149f -> 4
        in 150f..179f -> 5
        in 180f..209f -> 6
        in 210f..239f -> 7
        in 240f..269f -> 8
        in 270f..299f -> 9
        in 300f..329f -> 10
        in 330f..359f -> 11
        else -> 12 // Eğer bir hata oluşursa varsayılan olarak 12'yi döndür
    }
}
fun fromAngleToMinut(angle: Float):Int{
    val normalizedAngle =   angle +90
    return Math.abs((normalizedAngle/6).toInt())
}
private fun hourMinuteAmPm() :TimeInfo{
    val calendar = Calendar.getInstance() // Mevcut zamanı alır
    val hour = calendar.get(Calendar.HOUR) // 12 saatlik formatta saat bilgisini alır
    val amPm = calendar.get(Calendar.AM_PM) // AM veya PM değerini alır
val minute= calendar.get(Calendar.MINUTE)

    val amPmType = if (amPm == Calendar.AM) AmPm.AM else AmPm.AM

    return TimeInfo(hour, minute, amPmType)
}
private fun hourAndMinuteAngle(timeInfo: TimeInfo):HourMinuteAngle{
val hourAngle = (30*timeInfo.hour + timeInfo.minute/2 ).toFloat()
    val minuteAngle= (5*timeInfo.minute).toFloat()
    return HourMinuteAngle(hourAngle = hourAngle,minuteAngle=minuteAngle)
}

private fun DrawScope.ClockCircle(
    radius: Float,
    clockStyle: ClockStyle
) {
    drawContext.canvas.nativeCanvas.apply {


        drawCircle(center.x, center.y, radius, Paint().apply {
            color = clockStyle.colors.clockFaceColor.toArgb()
            style = Paint.Style.FILL

            setShadowLayer(
                50f,
                0f,
                0f,
                Color.argb(50, 0, 0, 0)
            )
        })

    }
}


private fun DrawScope.ClockNumbers(
    clockStyle: ClockStyle,
    hourOrMinute: ClockFaceType,
    radius: Float,

) {
    // Clock Numbers

    val paint = Paint().apply {
        textSize = 21.sp.toPx()
        textAlign = Paint.Align.CENTER
        color = clockStyle.colors.clockNumberColor.toArgb()
        // Diğer paint ayarları buraya eklenebilir
    }


    if (hourOrMinute == ClockFaceType.Hour) {
        for (index in 1..12) {
            val angleToRadyan = ((index * (360f / 12)) - 90f) * (PI / 180f).toFloat()
            val text =
                if (index < 10) " $index " else index.toString()  // 1'den 9'a kadar olan sayıları iki haneli olarak formatla


            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()

            val textX =
                (radius  * cos(angleToRadyan) + center.x) - textWidth * cos(
                    angleToRadyan
                )

            val textY =
                (radius   * sin(angleToRadyan) + center.y) - textHeight * sin(
                    angleToRadyan
                ) + textHeight / 2
            Log.d("Clock", "Clock: cos(angleInRad${cos(angleToRadyan)}")
            drawContext.canvas.nativeCanvas.drawText(text, textX, textY, paint)
        }
    } else {
        for (index in 1..60) {
            val angleToRadyan = ((index * (360f / 60)) - 90f) * (PI / 180f).toFloat()
            var text = ""
            if (index % 5 == 0) {
                text = index.toString()
            }

            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()

            val textX =
                (radius * cos(angleToRadyan) + center.x) - textWidth * cos(angleToRadyan)

            val textY =
                (radius * sin(angleToRadyan)+ center.y) - textHeight * sin(angleToRadyan)+ textHeight / 2
            Log.d("Clock", "Clock: cos(angleInRad${cos(angleToRadyan)}")
            drawContext.canvas.nativeCanvas.drawText(text, textX, textY, paint)
        }
    }
}


private fun DrawScope.smallAndLargeHandDotPointer(
    clockStyle: ClockStyle,
    radius: Float,

    angleToRadyanForDraw: Float,

    ) {
    //SmallHandDotpointer

    val radiusPointer = clockStyle.shapes.handPointDotRadius.toPx()
    val centerPointer = Offset(
        x = (radius   - clockStyle.shapes.handPointCircleRadius.toPx()/2-15) * cos(
            angleToRadyanForDraw
        ) + center.x,
        y = (radius - clockStyle.shapes.handPointCircleRadius.toPx()/2-15) * sin(
            angleToRadyanForDraw
        ) + center.y
    )
    drawCircle(
        color = clockStyle.colors.handPointDotColor,
        center = centerPointer,
        radius = radiusPointer
    )


//LArgeHandDotpointer


    drawCircle(
        color = clockStyle.colors.handPointCircleColor,
        center = centerPointer,
        radius = clockStyle.shapes.handPointCircleRadius.toPx()
    )
}

fun calculateAngle(centerX: Float, centerY: Float, touchX: Float, touchY: Float): Float {
    val deltaX = touchX - centerX
    val deltaY = touchY - centerY
    val angleInRadians = atan2(deltaY, deltaX)
    return Math.toDegrees(angleInRadians.toDouble()).toFloat()
}


private fun DrawScope.HourHand(
    radius: Float,

    clockStyle: ClockStyle,
    angleToRadyanForDraw: Float,

    ) {

    val endOffset = Offset(
        x = ( radius  - clockStyle.shapes.handPointCircleRadius.toPx()/2-15) * cos(
            angleToRadyanForDraw
        ) + center.x, // radius'ten 20 birim çıkar
        y = (radius  - clockStyle.shapes.handPointCircleRadius.toPx()/2) * sin(
            angleToRadyanForDraw
        ) + center.y  // radius'ten 20 birim çıkar
    )
    drawLine(
        color = clockStyle.colors.hourHandsColor,
        start = Offset(center.x, center.y),
        end = endOffset,
        strokeWidth = 6F
    )
}


private fun DrawScope.ClockCenter(clockStyle: ClockStyle) {
    drawCircle(
        color = clockStyle.colors.centerPointColor,
        clockStyle.shapes.centerPointRadius.toPx(),
        center = Offset(center.x, center.y)
    )
}


@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    TimePicker()
}
fun main(){
    hourMinuteAmPm()
    print(hourAndMinuteAngle(hourMinuteAmPm()).hourAngle)
}