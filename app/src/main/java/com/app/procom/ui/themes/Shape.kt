package com.app.procom.ui.themes

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.app.procom.util.view.px


val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = CutCornerShape(topStart = 16.dp),
    large = RoundedCornerShape(8.dp)
)

@Immutable
data class AppShapes(
    val small: Shape,
    val smallTopRounded: Shape,
    val smallBottomRounded: Shape,
    val circle: Shape
)

val LocalAppShapes = staticCompositionLocalOf {
    AppShapes(
        small = RoundedCornerShape(ZeroCornerSize),
        smallTopRounded = RoundedCornerShape(ZeroCornerSize),
        smallBottomRounded = RoundedCornerShape(ZeroCornerSize),
        circle = RoundedCornerShape(ZeroCornerSize),
    )
}

val appShapes = AppShapes(
    small = RoundedCornerShape(size = 8.px),
    smallTopRounded = RoundedCornerShape(topStart = 8.px, topEnd = 8.px),
    smallBottomRounded = RoundedCornerShape(bottomStart = 8.px, bottomEnd = 8.px),
    circle = RoundedCornerShape(percent = 50),
)


