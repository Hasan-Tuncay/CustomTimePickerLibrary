package com.hasantuncay.myapplication.customTimePicker

import android.graphics.Paint
import android.graphics.Rect

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import java.lang.Math.toDegrees
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimePickerCustomizable(
    modifier: Modifier = Modifier,
    clockStyle: ClockStyle = ClockStyle(),
    onTimeInfo: (TimeInfo) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var columnBounds by remember {
        mutableStateOf<
                androidx.compose.ui.geometry.Rect?>(null)
    }
    val timeInfo = hourMinuteAmPm()
    var amPmButtonState = remember {
        mutableStateOf(timeInfo.amPm)
    }
    var timeInfoState = remember {
        mutableStateOf(hourMinuteAmPm())
    }
//    var onDone by remember {
//        mutableStateOf(false)
//    }


//    LaunchedEffect(key1 = onDone, block = {
//        onTimeInfo(timeInfoState.value)
//
//    })
    var hourOrMinuteState by remember {
        mutableStateOf<ClockFaceType>(ClockFaceType.HOUR)
    }
    LaunchedEffect(key1 = timeInfoState.value, block = {

    })
//    if (onDone == false) {
        BoxWithConstraints(
            modifier = modifier

                .wrapContentSize()
                .background(clockStyle.colors.genaralBackgrgroundColor)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { offset ->
                        // Column'ın sınırlarını ve koordinatlarını kontrol et
                        columnBounds?.let { bounds ->
                            bounds.height
                            // Tıklama koordinatlarının, Column'ın sınırları dışında olup olmadığını kontrol et
                            if (!bounds.contains(offset)) {
                              //  onDone = true
//                                Log.d(
//                                    "ScreenSettings",
//                                    "offset = ${offset}" +
//                                            "column bounds = ${columnBounds}"
//                                )
                            }
                        }

                    })
                }


        ) {

            val constraintsScope = this

            val clockShapes = with(LocalDensity.current) {
                ClockShapes(
                    clockfaceRadius = constraintsScope.maxWidth.toPx() * 0.35f,
                    centerPointRadius = 3.dp.toPx(),
                    handPointDotRadius = 5.dp.toPx(),
                    handPointCircleRadius = (constraintsScope.maxWidth.toPx() * 0.35f) * 0.13F,
                    handWwidth = 2.dp.toPx()
                )
            }

            val dynamicClockStyle = clockStyle.copy(shapes = clockShapes)
            val radius = with(LocalDensity.current) { clockStyle.shapes.clockfaceRadius }
            Log.d(
                "ScreenSettings", "TimePicker: ClockShapes = ${clockShapes}" +
                        "screenwidthCurrent = ${screenWidth}" +
                        "column bounds = ${columnBounds}"
            )
            Column(
                Modifier
                    .align(Alignment.Center)
                    .width(constraintsScope.maxWidth * 0.90f)
                    .background(color = androidx.compose.ui.graphics.Color.Transparent)
                    .border(0.5.dp, color = clockStyle.header.headeUnselectedColor)
                    .onGloballyPositioned { coordinates ->
                        val parentCoordinates = coordinates.parentCoordinates
                        columnBounds = coordinates.boundsInWindow()
                        // columnBounds?.let { onPositionChanged(it) }
                    }

            ) {

                CustomTimeDisplay(
                    hourOrMinuteState,
                    timeInfoState,
                    amPmButtonState,
                    onClockFaceChange = {
                        hourOrMinuteState = it
                        Log.d(
                            "hourOrMinuteState",
                            "onClockFaceChange CustomTimeDisplay: ${hourOrMinuteState}"
                        )
                    })


                TimePickerBody(
                    hourOrMinuteState,
                    dynamicClockStyle,
                    timeInfoState,
                    amPmButtonState,
                    onClockFaceChange = { clockfaceType ->
                        hourOrMinuteState = clockfaceType
                        Log.d("hourOrMinuteState", "onClockFaceChange: ${hourOrMinuteState}")
                    },
                    onTimeInfoState = {
                        timeInfoState.value = it
                        Log.d("hourOrMinuteState", "onTimeInfoState: ${timeInfoState}")
                    })

                BottomButton(clockStyle, {   onTimeInfo(timeInfoState.value)})
            }
        }

    }



@Composable
fun ShowTimePickerDialog(
    isDialogOpen: MutableState<Boolean>,
    onTimeSelected: (TimeInfo) -> Unit
) {

        AlertDialog(
            onDismissRequest = { isDialogOpen.value = false },
            title = { Text(text = "Zaman Seçin") },
            text = {
                TimePickerCustomizable(
                    modifier = Modifier.fillMaxWidth(),
                    clockStyle = ClockStyle(), // Stilinizi burada geçin
                    onTimeInfo = { timeInfo ->
                        onTimeSelected(timeInfo)
                        isDialogOpen.value = false // Zaman seçildikten sonra diyalogu kapat
                    }
                )
            },
            confirmButton = {},
            dismissButton = {}
        )

}

@Composable
fun ShowCustomDialog(
    isDialogOpen: MutableState<Boolean>,
    onTimeSelected: (TimeInfo) -> Unit
) {
    Log.d("hourOrMinuteState", "isDialogOpen: ${isDialogOpen}")

    Log.d("hourOrMinuteState", "isDialogOPenInside: ${isDialogOpen}")
    if (isDialogOpen.value) {
        Dialog(onDismissRequest = {
            isDialogOpen.value = false
        }) {
            Log.d("hourOrMinuteState", "onClockFaceChange: ${isDialogOpen}")
            TimePickerCustomizable(
                clockStyle = ClockStyle(), // Stilinizi burada geçin
                onTimeInfo = { timeInfo ->
                    onTimeSelected(timeInfo)
                    isDialogOpen.value = false // Zaman seçildikten sonra diyalogu kapat
                }
            )
        }
    }
}
@Composable
fun CustomTimeDisplay(
    hourOrMinuteState: ClockFaceType,
    timeInfoState: MutableState<TimeInfo>,
    amPmButtonState: MutableState<AmPm>,
    clockStyle: ClockStyle = ClockStyle(),
    onClockFaceChange: (ClockFaceType) -> Unit
) {
//    val timeInfoState by remember {
//        mutableStateOf(timeInfoState)
//    }

    Log.d(
        "hourOrMinuteState",
        "TimePicker out: ${timeInfoState.value.hour}\n" + "${timeInfoState.value.minute}\n" + "${timeInfoState.value.amPm}"
    )
    val (hourTextColor, minuteTextColor) = when (hourOrMinuteState) {
        ClockFaceType.HOUR -> Pair(
            clockStyle.header.headerSelectedColor, clockStyle.header.headeUnselectedColor
        )

        else -> Pair(clockStyle.header.headeUnselectedColor, clockStyle.header.headerSelectedColor)

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(clockStyle.header.headerBackgroundColor),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        // Saat kısmı
        Text(
            text = String.format("%02d", timeInfoState.value.hour),
            color = hourTextColor,
            fontSize = 60.sp,
            modifier = Modifier
                .align(Alignment.Bottom)
                .clickable {
                    onClockFaceChange(ClockFaceType.HOUR)
                }
        )
        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        // Noktaları çizmek için Canvas kullanımı
        SeparatorDots(clockStyle = clockStyle)
        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        // Dakika kısmı
        Text(
            text = String.format("%02d", timeInfoState.value.minute),
            fontSize = 60.sp,
            color = minuteTextColor,
            modifier = Modifier
                .align(Alignment.Bottom)
                .clickable {
                    onClockFaceChange(ClockFaceType.MINUTE)
                }
        )

        // AM/PM kısmı
        Text(
            text = timeInfoState.value.amPm.toString(),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(bottom = 60.sp.value.dp / 4)
                .align(Alignment.Bottom)

        )
    }
}

@Composable
private fun TimePickerBody(
    hourOrMinuteState: ClockFaceType,
    clockStyle: ClockStyle,
    timeInfoState: MutableState<TimeInfo>,
    amPmButtonState: MutableState<AmPm>,
    onTimeInfoState: (TimeInfo) -> Unit,
    onClockFaceChange: (ClockFaceType) -> Unit
) {
    Log.d("hourOrMinuteState", "onClockFaceChange TimePickerBody: ${hourOrMinuteState}")
    Box(modifier = Modifier
        .zIndex(1f)
        .wrapContentHeight()
        .background(clockStyle.body.bodyBackgroundColor)
        .drawBehind {

        }) {

        val dpValue = with(LocalDensity.current) { clockStyle.shapes.clockfaceRadius.toDp() * 2.2f }

        Clock(modifier = Modifier
            .fillMaxWidth()
            .height(dpValue)
            .clip(
                CircleShape
            )

            .align(
                Alignment.TopCenter
            )
            .padding(top = 8.dp)


            .graphicsLayer {

            },
            timeInfoState,
            hourOrMinuteState = hourOrMinuteState,
            clockStyle = clockStyle,
            onClockFaceChange = {
                onClockFaceChange(it)
            })
        { timeInfo ->

            onTimeInfoState(timeInfo)
            Log.d(
                "hourOrMinuteState", "Clock: min end   ${timeInfo}"
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)

                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                )
                .graphicsLayer {

                    translationY = clockStyle.shapes.clockfaceRadius * 1.75f
                },
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val (amButtonColor, pmButtonColor) = when (amPmButtonState.value) {
                AmPm.AM -> Pair(
                    clockStyle.body.amPmSelectedColor, clockStyle.body.amPmUnselectedColor
                ) // AM seçiliyse
                AmPm.PM -> Pair(
                    clockStyle.body.amPmUnselectedColor, clockStyle.body.amPmSelectedColor
                ) // PM seçiliyse
            }

            TextButton(
                onClick = {

                    amPmButtonState.value = AmPm.AM
                    val timeInfoStateCopy = timeInfoState.value.copy()
                    timeInfoStateCopy.amPm = AmPm.AM
                    onTimeInfoState(timeInfoStateCopy)
                },
                modifier = Modifier
                    .background(amButtonColor, CircleShape)
                    .size(50.dp),
                shape = CircleShape,
                contentPadding = PaddingValues()
            ) {

                Text(AmPm.AM.name, color = clockStyle.body.amPmFontColor)
            }


            TextButton(
                onClick = {
                    amPmButtonState.value = AmPm.PM
                    val timeInfoStateCopy = timeInfoState.value.copy()
                    timeInfoStateCopy.amPm = AmPm.PM
                    onTimeInfoState(timeInfoStateCopy)
                },
                modifier = Modifier
                    .background(pmButtonColor, CircleShape)

                    .size(50.dp),
                shape = CircleShape,
                contentPadding = PaddingValues()
            ) {

                Text(AmPm.PM.name, color = clockStyle.body.amPmFontColor)
            }

        }

    }
}

@Composable
private fun BottomButton(clockStyle: ClockStyle, onDone: () -> Unit) {
    Divider(thickness = 0.5.dp, color = clockStyle.bottom.bottomSeparatorColor)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        verticalAlignment = Alignment.CenterVertically // Yatayda merkeze hizala, ancak bu Row için geçerli değil.
    ) {
        Button(
            onClick = { onDone() },
            modifier = Modifier
                .fillMaxWidth() // Butonun genişliğini ekrana sığdır
                .height(52.dp), // Butonun yüksekliğini 52.dp yap
            shape = RoundedCornerShape(0.dp), // Köşe yuvarlaklıklarını ayarla (Dikdörtgen için 0.dp)
            colors = ButtonDefaults.buttonColors(containerColor = clockStyle.bottom.bottomBackgroundColor) // Butonun arka plan rengini ayarla
        ) {
            Text(
                "Done", color = clockStyle.bottom.bottomButtonFontColor, fontSize = 18.sp
            ) // Buton üzerindeki metin ve rengi
        }
    }
}


@OptIn(ExperimentalTextApi::class)
@Composable
fun Clock(
    modifier: Modifier = Modifier,
    timeInfo: MutableState<TimeInfo>,
    hourOrMinuteState: ClockFaceType,
    onClockFaceChange: (ClockFaceType) -> Unit,
    clockStyle: ClockStyle = ClockStyle(),
    onTimeSelected: (TimeInfo) -> Unit
) {
    Log.d("hourOrMinuteState", "onClockFaceChange Clock out: ${hourOrMinuteState}")
    var hourOrMinuteStateIn by remember {
        mutableStateOf<ClockFaceType>(hourOrMinuteState)
    }

    Log.d("hourOrMinuteState", "onClockFaceChange Clock: ${hourOrMinuteState}")
    var clockStyle by remember {
        mutableStateOf(clockStyle)
    }
    LaunchedEffect(key1 = hourOrMinuteState, block = {
        hourOrMinuteStateIn = hourOrMinuteState
        Log.d("hourOrMinuteState", "onClockFaceChange Clock launch ${hourOrMinuteState}")
    })
    // DP birimini piksele çevirme


    val radius = clockStyle.shapes.clockfaceRadius
    val radiusInsideComponent = radius - 30f

    var hourHandAngle by remember {
        mutableStateOf(hourAndMinuteAngle(hourMinuteAmPm()).hourAngle - 90)
    }
    var dd2 = 0F

    Canvas(modifier = modifier
        .zIndex(1f)

        .pointerInput(Unit) {
            detectDragGestures(

                onDrag = { change, _ ->
                    Log.d(
                        "Angle", "Clock: angle ${change}"
                    )
                    val centerX = (size.width / 2).toFloat()
                    val centerY = (size.height / 2).toFloat()
                    // Kullanıcının dokunma noktasına göre açıyı hesaplayın
                    hourHandAngle =
                        calculateAngle(centerX, centerY, change.position.x, change.position.y)
                    dd2 = calculateAngleForClock(
                        centerX, centerY, change.position.x, change.position.y
                    )
                    Log.d(
                        "Angle", "Clock: angle ${
                            calculateAngle(
                                centerX, centerY, change.position.x, change.position.y
                            )
                        }"
                    )
                }, onDragEnd = {
                    val timeInfo = timeInfo.value.copy()
//  val timeInfo=TimeInfo(timeInfo.value.hour,timeInfo.value.hour,timeInfo.value.amPm)


                    when (hourOrMinuteStateIn) {
                        ClockFaceType.HOUR -> {
                            Log.d(
                                "hourOrMinuteState",
                                "onClockFaceChange Clock when: ${hourOrMinuteState}"
                            )
                            timeInfo.hour = fromAngleToHour(hourHandAngle)
                            onTimeSelected(timeInfo)
                            Log.d(
                                "hourOrMinuteState", "Clock: hour end ${
                                    fromAngleToHour(dd2)
                                } "
                            )

                        }

                        else -> {
                            Log.d(
                                "hourOrMinuteState",
                                "onClockFaceChange Clock when: ${hourOrMinuteState}"
                            )
                            timeInfo.minute = fromAngleToMinute(dd2)
                            //    fromAngleToMinute(dd2)
                            onTimeSelected(timeInfo)
                            Log.d(
                                "hourOrMinuteState",
                                "Clock: min end   ${fromAngleToMinute(dd2)}"
                            )
                        }
                    }
                    hourOrMinuteStateIn = ClockFaceType.MINUTE
                    onClockFaceChange(ClockFaceType.MINUTE)


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
    Log.d("fromAngleToMinut", "fromAngleToMinut: $angle")
    val normalizedAngle = if (angle < 0) (angle % 360) + 360 else angle % 360
    return when (normalizedAngle) {
        in 0f..29f -> 3
        in 30f..59f -> 4
        in 60f..89f -> 5
        in 90f..119f -> 6
        in 120f..149f -> 7
        in 150f..179f -> 8
        in 180f..209f -> 9
        in 210f..239f -> 10
        in 240f..269f -> 11
        in 270f..299f -> 12
        in 300f..329f -> 1
        in 330f..359f -> 2
        else -> 12 // Eğer bir hata oluşursa varsayılan olarak 12'yi döndür
    }
}

fun calculateAngleForClock(centerX: Float, centerY: Float, touchX: Float, touchY: Float): Float {
    val deltaX = touchX - centerX
    val deltaY = touchY - centerY
    val angleInRadians = atan2(deltaY, deltaX)
    var angleInDegrees = Math.toDegrees(angleInRadians.toDouble()).toFloat()

    // Açıyı saat yönünde ve 12'nin pozisyonundan başlayacak şekilde ayarla
    angleInDegrees = (angleInDegrees + 90) % 360
    if (angleInDegrees < 0) {
        angleInDegrees += 360
    }

    return angleInDegrees
}

fun fromAngleToMinute(angle: Float): Int {
    // Açıyı saat tasarımına göre 360 dereceye göre mod al
    val normalizedAngle = if (angle < 0) (angle % 360) + 360 else angle % 360
    // Normalleştirilmiş açıyı dakikaya çevir
    return (normalizedAngle / 6).toInt()
}

//fun fromAngleToMinut(angle: Float): Int {
//    val normalizedAngle = angle
//    return (normalizedAngle / 6).toInt()
//}
fun fromAngleToMinut(angle: Float): Int {

    val normalizedAngle = angle + 90


    return (normalizedAngle / 6).toInt()
}


private fun hourMinuteAmPm(): TimeInfo {
    val calendar = Calendar.getInstance() // Mevcut zamanı alır
    val hour = calendar.get(Calendar.HOUR) // 12 saatlik formatta saat bilgisini alır
    val amPm = calendar.get(Calendar.AM_PM) // AM veya PM değerini alır
    val minute = calendar.get(Calendar.MINUTE)

    val amPmType = if (amPm == Calendar.AM) AmPm.AM else AmPm.AM

    return TimeInfo(hour, minute, amPmType)
}

private fun hourAndMinuteAngle(timeInfo: TimeInfo): HourMinuteAngle {
    val hourAngle = (30 * timeInfo.hour + timeInfo.minute / 2).toFloat()
    val minuteAngle = (5 * timeInfo.minute).toFloat()
    return HourMinuteAngle(hourAngle = hourAngle, minuteAngle = minuteAngle)
}

private fun DrawScope.ClockCircle(
    radius: Float, clockStyle: ClockStyle
) {

    drawContext.canvas.nativeCanvas.apply {


        drawCircle(center.x, center.y, radius, Paint().apply {
            color = clockStyle.colors.clockFaceColor.toArgb()
            style = Paint.Style.FILL

//            setShadowLayer(
//                50f,
//                0f,
//                0f,
//                Color.argb(50, 0, 0, 0)
//            )
        })

    }
}

@Composable
fun SeparatorDots(modifier: Modifier = Modifier, clockStyle: ClockStyle = ClockStyle()) {
    Box(modifier = modifier) {
        val spaceBetween = with(LocalDensity.current) { 12.dp.toPx() * 2.2f }
        Canvas(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.Center)
                .size(width = 10.dp, height = 60.sp.value.dp)
        ) {
            val dotSize = 4.dp.toPx()


            drawCircle(clockStyle.header.headerSeperateDotColor, dotSize, center = center.copy(y = center.y - spaceBetween / 2f ,x=center.x -dotSize))
            drawCircle(clockStyle.header.headerSeperateDotColor, dotSize, center = center.copy(y = center.y + spaceBetween / 3f,x=center.x -dotSize))
        }
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


    if (hourOrMinute == ClockFaceType.HOUR) {
        for (index in 1..12) {
            val angleToRadyan = ((index * (360f / 12)) - 90f) * (PI / 180f).toFloat()
            val text =
                if (index < 10) " $index " else index.toString()  // 1'den 9'a kadar olan sayıları iki haneli olarak formatla


            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()

            val textX = (radius * cos(angleToRadyan) + center.x) - textWidth * cos(
                angleToRadyan
            )

            val textY = (radius * sin(angleToRadyan) + center.y) - textHeight * sin(
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

            val textX = (radius * cos(angleToRadyan) + center.x) - textWidth * cos(angleToRadyan)

            val textY =
                (radius * sin(angleToRadyan) + center.y) - textHeight * sin(angleToRadyan) + textHeight / 2
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

    val radiusPointer = clockStyle.shapes.handPointDotRadius
    val centerPointer = Offset(
        x = (radius - clockStyle.shapes.handPointCircleRadius / 2) * cos(
            angleToRadyanForDraw
        ) + center.x, y = (radius - clockStyle.shapes.handPointCircleRadius / 2) * sin(
            angleToRadyanForDraw
        ) + center.y
    )
    drawCircle(
        color = clockStyle.colors.handPointDotColor, center = centerPointer, radius = radiusPointer
    )


//LArgeHandDotpointer


    drawCircle(
        color = clockStyle.colors.handPointCircleColor,
        center = centerPointer,
        radius = clockStyle.shapes.handPointCircleRadius
    )
}

fun calculateAngle2(centerX: Float, centerY: Float, touchX: Float, touchY: Float): Float {
    val deltaX = touchX - centerX
    val deltaY = touchY - centerY
    val angleInRadians = atan2(deltaY, deltaX)
    return Math.toDegrees(angleInRadians.toDouble()).toFloat()
}


fun calculateAngle(centerX: Float, centerY: Float, touchX: Float, touchY: Float): Float {
    val deltaX = touchX - centerX
    val deltaY = touchY - centerY
    val angleInRadians = atan2(deltaY, deltaX)
    var angleInDegrees = toDegrees(angleInRadians.toDouble()).toFloat()
    if (angleInDegrees < 0) {
        angleInDegrees += 360  // Negatif açıları pozitife çevir
    }
    return angleInDegrees
}


private fun DrawScope.HourHand(
    radius: Float,

    clockStyle: ClockStyle,
    angleToRadyanForDraw: Float,

    ) {

    val endOffset = Offset(
        x = (radius - clockStyle.shapes.handPointCircleRadius / 2) * cos(
            angleToRadyanForDraw
        ) + center.x, // radius'ten 20 birim çıkar
        y = (radius - clockStyle.shapes.handPointCircleRadius / 2) * sin(
            angleToRadyanForDraw
        ) + center.y  // radius'ten 20 birim çıkar
    )
    drawLine(
        color = clockStyle.colors.hourHandsColor,
        start = Offset(center.x, center.y),
        end = endOffset,
        strokeWidth = clockStyle.shapes.handWwidth
    )
}


private fun DrawScope.ClockCenter(clockStyle: ClockStyle) {
    drawCircle(
        color = clockStyle.colors.centerPointColor,
        clockStyle.shapes.centerPointRadius,
        center = Offset(center.x, center.y)
    )
}


@Preview(showBackground = true)
@Composable
fun ClockPreview() {
   // ShowTimePickerDialog(isDialogOpen = mutableStateOf(true), onTimeSelected ={} )
  //  ShowCustomDialog(mutableStateOf(true),{})
    TimePickerCustomizable() {
        Log.d("ClockPreview", "ClockPreview: ${it}")
     }
    // Clock(modifier = Modifier.fillMaxSize())
}

fun main() {
    hourMinuteAmPm()
    print(hourAndMinuteAngle(hourMinuteAmPm()).hourAngle)
}