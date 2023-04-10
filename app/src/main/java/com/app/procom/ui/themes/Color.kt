package com.app.procom.ui.themes

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val BlueSecondary = Color(0xff6EC0FF)
val BluePrimary = Color(0xff1890FF)
val BlueDark = Color(0xff0063CB)
val BlueSecondary2 = Color(0xff548AF2)
val GrayPrimary = Color(0xff1A1B1D)
val GrayPrimaryDark = Color(0xff686E77)
val RedPrimary = Color(0xffD84B4B)
val RedSecondary = Color(0xffFF6F61)
val ProgressBarBackground = Color(0xff1A1B1D)
val ProgressSteps = Color(0xffF7F8FA)
val CoralBlue = Color(0xFFC3D8EC)
val CoralBlueLight = Color(0xFFDCEEFF)
val CoralBlueDark = Color(0xFF7392AD)

@Immutable
data class ColorTones(
    val light: Color,
    val secondary: Color,
    val primary: Color,
    val dark: Color,
) {
    companion object {
        @Stable
        val Unspecified: ColorTones = ColorTones(
            light = Color.Unspecified,
            secondary = Color.Unspecified,
            primary = Color.Unspecified,
            dark = Color.Unspecified,
        )
    }
}

@Immutable
data class ColorBrand(
    val coral: Color,
    val gibraltar: Color,
) {
    companion object {
        @Stable
        val Unspecified: ColorBrand = ColorBrand(
            coral = Color.Unspecified,
            gibraltar = Color.Unspecified,
        )
    }
}

@Immutable
data class AppColors(
    val background: Color,
    val onBackground: Color,
    val overlay: Color,
    val coralBlue: ColorTones,

    val gray: ColorTones,
    val blue: ColorTones,
    val red: ColorTones,
    val green: ColorTones,
    val yellow: ColorTones,
    val brand: ColorBrand,
)

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        overlay = Color.Unspecified,
        coralBlue = ColorTones.Unspecified,

        gray = ColorTones.Unspecified,
        blue = ColorTones.Unspecified,
        red = ColorTones.Unspecified,
        green = ColorTones.Unspecified,
        yellow = ColorTones.Unspecified,
        brand = ColorBrand.Unspecified
    )
}

val appColors = AppColors(
    background = Color.Black,
    onBackground = Color.White,
    overlay = ProgressBarBackground,
    coralBlue = ColorTones(
        light = CoralBlueLight,
        dark = CoralBlueDark,
        primary = CoralBlue,
        secondary = CoralBlue
    ),
    gray = ColorTones(
        light = ProgressSteps,
        secondary = Color(0xFFDBE1EB),
        primary = GrayPrimaryDark,
        dark = Color(0xFF09090A),
    ),
    blue = ColorTones(
        light = Color(0xFFF2F8FF),
        secondary = BlueSecondary,
        primary = BluePrimary,
        dark = BlueDark,
    ),
    red = ColorTones(
        light = Color(0xFFFFF2F2),
        secondary = Color(0xFFFF7D77),
        primary = RedPrimary,
        dark = Color(0xFFA01023),
    ),
    green = ColorTones(
        light = Color(0xFFE8F5E9),
        secondary = Color(0xFF76D275),
        primary = Color(0xFF43A047),
        dark = Color(0xFF00701A),
    ),
    yellow = ColorTones(
        light = Color(0xFFFFFDE7),
        secondary = Color(0xFFFFFF85),
        primary = Color(0xFFF2CC54),
        dark = Color(0xFFBC9B20),
    ),
    brand = ColorBrand(
        coral = RedSecondary,
        gibraltar = Color(0xFF123850)
    )
)


