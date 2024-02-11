//package com.hasantuncay.myapplication.customTimePicker
//
//import androidx.compose.runtime.*
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.input.pointer.pointerInput
//import kotlin.math.*
//
//@Composable
//fun CustomTimePicker() {
//    // Saat elinin açısını depolamak için bir durum değişkeni
//    var handAngle by remember { mutableStateOf(0f) }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Canvas(modifier = Modifier
//            .fillMaxSize()
//            .pointerInput(Unit) {
//                detectDragGestures(onDrag = { change, _ ->
//                    val centerX = size.width / 2
//                    val centerY = size.height / 2
//                    // Kullanıcının dokunma noktasına göre açıyı hesaplayın
//                    handAngle = calculateAngle(centerX, centerY, change.position.x, change.position.y)
//                })
//            }) {
//            drawHand(centerX = size.width / 2, centerY = size.height / 2, angle = handAngle, radius = 100f) // radius, saatin elinin uzunluğunu temsil eder
//        }
//    }
//}
//
//fun DrawScope.drawHand(centerX: Float, centerY: Float, angle: Float, radius: Float) {
//    val angleInRadians = Math.toRadians(angle.toDouble()).toFloat()
//    val handEndX = centerX + radius * cos(angleInRadians)
//    val handEndY = centerY + radius * sin(angleInRadians)
//
//    // Saatin elini çizin
//    drawLine(
//        start = Offset(centerX, centerY),
//        end = Offset(handEndX, handEndY),
//        strokeWidth = 4f // Saatin elinin kalınlığı
//    )
//}
//
//fun calculateAngle(centerX: Float, centerY: Float, touchX: Float, touchY: Float): Float {
//    val deltaX = touchX - centerX
//    val deltaY = touchY - centerY
//    val angleInRadians = atan2(deltaY, deltaX)
//    return Math.toDegrees(angleInRadians.toDouble()).toFloat() + 180 // Açıyı derece cinsinden döndür
//}
