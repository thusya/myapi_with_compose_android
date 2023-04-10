@file:Suppress("MagicNumber")

package com.app.procom.ui.themes

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.app.procom.R
import com.app.procom.util.view.spx


val robotoFonts = FontFamily(
    Font(R.font.roboto_italic, weight = FontWeight.Bold),
    Font(R.font.roboto_medium_italic, weight = FontWeight.Bold),
    Font(R.font.roboto_bold_italic, weight = FontWeight.Bold),
    Font(R.font.roboto_regular, weight = FontWeight.Normal),
    Font(R.font.roboto_medium, weight = FontWeight.Normal),
    Font(R.font.roboto_bold, weight = FontWeight.Bold)
)


val typography = Typography(
    h1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 16.sp,
        fontFamily = robotoFonts
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        fontFamily = robotoFonts
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        fontFamily = robotoFonts
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        fontFamily = robotoFonts
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 22.sp,
        fontFamily = robotoFonts
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 20.sp,
        fontFamily = robotoFonts
    )
)

@Immutable
data class AppTypography(
    val title: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
    val numbering: TextStyle,
    val label: TextStyle,
)

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        title = TextStyle.Default,
        body = TextStyle.Default,
        caption = TextStyle.Default,
        numbering = TextStyle.Default,
        label = TextStyle.Default,
    )
}

val appTypography = AppTypography(
    title = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.spx,
        lineHeight = 32.spx,
        fontFamily = robotoFonts
    ),
    body = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.spx,
        lineHeight = 32.spx,
        fontFamily = robotoFonts
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 20.spx,
        lineHeight = 24.spx,
        fontFamily = robotoFonts,
        letterSpacing = (-0.4f).spx
    ),
    numbering = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.spx,
        lineHeight = 32.spx,
        fontFamily = robotoFonts,
        letterSpacing = (-0.64f).spx
    ),
    label = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 48.spx,
        lineHeight = 56.spx,
        fontFamily = robotoFonts,
    )
)


