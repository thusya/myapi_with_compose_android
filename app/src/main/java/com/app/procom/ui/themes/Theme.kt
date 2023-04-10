package com.app.procom.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColors = appColors
    val appTypography = appTypography
    val appShapes = appShapes

    val appRippleTheme = AppRippleTheme(
        defaultColor = appColors.blue.primary,
        rippleAlpha = RippleAlpha(
            draggedAlpha = 0.3f,
            focusedAlpha = 0.3f,
            hoveredAlpha = 0.3f,
            pressedAlpha = 0.3f
        )
    )

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalAppTypography provides appTypography,
        LocalAppShapes provides appShapes,
        LocalRippleTheme provides appRippleTheme,
        content = content
    )
}

@Immutable
class AppRippleTheme(
    val defaultColor: Color,
    val rippleAlpha: RippleAlpha
) : RippleTheme {
    @Composable
    override fun defaultColor() = defaultColor

    @Composable
    override fun rippleAlpha() = rippleAlpha
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current
    val shapes: AppShapes
        @Composable
        get() = LocalAppShapes.current
}


