package com.gukunov.scannerapp.scanner

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Затемнение вокруг квадратной рамки-прицела (чисто декоративное). */
@Composable
fun ViewfinderOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val side = size.minDimension * 0.65f
        val topLeft = Offset((size.width - side) / 2f, (size.height - side) / 2f)
        val frameSize = Size(side, side)
        val corner = CornerRadius(24.dp.toPx())

        // EvenOdd: закрашивается весь экран, кроме «дырки» на месте рамки.
        val dim = Path().apply {
            fillType = PathFillType.EvenOdd
            addRect(Rect(Offset.Zero, size))
            addRoundRect(RoundRect(Rect(topLeft, frameSize), corner))
        }
        drawPath(dim, Color.Black.copy(alpha = 0.5f))
        drawRoundRect(
            color = Color.White,
            topLeft = topLeft,
            size = frameSize,
            cornerRadius = corner,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}
