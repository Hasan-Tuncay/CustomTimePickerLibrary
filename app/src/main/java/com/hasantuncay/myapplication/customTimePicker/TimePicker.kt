package com.hasantuncay.myapplication.customTimePicker

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp
import com.hasantuncay.myapplication.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt


@OptIn(ExperimentalTextApi::class)
@Composable
fun Clock(clockStyle: ClockStyle = ClockStyle()) {
    val context = LocalContext.current
    val radius = clockStyle.shapes.clockfaceRadius.value
    val hourOrMinute by remember {
        mutableStateOf(ClockFaceType.Hour)
    }
    val angle = remember {
        mutableStateOf(360f)
    }
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val layoutDirection = LocalLayoutDirection.current
    Canvas(modifier = Modifier.fillMaxSize()) {


// Clock Circle

        drawContext.canvas.nativeCanvas.apply {


            drawCircle(center.x, center.y, radius, Paint().apply {
                color = clockStyle.colors.clockFace.toArgb()
                style = Paint.Style.FILL

                setShadowLayer(
                    50f,
                    0f,
                    0f,
                    android.graphics.Color.argb(50, 0, 0, 0)
                )
            })

        }


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
                    (radius * cos(angleToRadyan) + center.x) - textWidth * cos(angleToRadyan)

                val textY =
                    (radius * sin(angleToRadyan) + center.y) - textHeight * sin(angleToRadyan) + textHeight / 2
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
                    (radius * sin(angleToRadyan) + center.y) - textHeight * sin(angleToRadyan)
                Log.d("Clock", "Clock: cos(angleInRad${cos(angleToRadyan)}")
                drawContext.canvas.nativeCanvas.drawText(text, textX, textY, paint)
            }
        }


// Clock center
        drawCircle(
            color = clockStyle.colors.centerPointColor,
            clockStyle.shapes.centerPointRadius.toPx(),
            center = Offset(center.x, center.y)
        )
// Clock center
        drawCircle(
            color = clockStyle.colors.centerPointColor,
            clockStyle.shapes.centerPointRadius.toPx(),
            center = Offset( center.x,  center.y)
        )
        // HourHand
        // HourHand
        val angleToRadyan = ( -90f) * (PI / 180f).toFloat()
        val endOffset =Offset(
            x = (radius-75) * cos(angleToRadyan)+center.x, // radius'ten 20 birim çıkar
            y = (radius-75) * sin(angleToRadyan)+center.y  // radius'ten 20 birim çıkar
        )
        drawLine(
            color = clockStyle.colors.hourHandsColor,
            start = Offset(center.x, center.y),
            end = endOffset,
            strokeWidth = 6F
        )



    }

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ClockNumbers(clockStyle: ClockStyle = ClockStyle()) {
    val context = LocalContext.current
    val radius = clockStyle.shapes.clockfaceRadius.value
    val hourOrMinute by remember {
        mutableStateOf(true)
    }
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val layoutDirection = LocalLayoutDirection.current
    Canvas(modifier = Modifier.fillMaxSize()) {
        val textListInt =
            if (hourOrMinute) ClockFaceType.Hour.hours else ClockFaceType.Minute.minutes

        for (index in textListInt.indices) {
            val angleInRad = ((index + 1) * (360f / textListInt.size) - 90) * (PI / 180f).toFloat()

            val text = AnnotatedString(textListInt[index])
            val style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            // Tahmini metin boyutlarını kullanarak metni ortala
            val textWidth = 30f  // Tahmini metin genişliği
            val textHeight = 20f  // Tahmini metin yüksekliği

            val textX = ((radius - textWidth * cos(angleInRad) + center.x))
            val textY = ((radius - textHeight * sin(angleInRad) + center.y))

            val textMeasurer = TextMeasurer(
                fallbackFontFamilyResolver = fontFamilyResolver,
                fallbackDensity = density,
                fallbackLayoutDirection = layoutDirection
            )

            drawText(textMeasurer, text, style = style, topLeft = Offset(textX, textY))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    Clock()
}
