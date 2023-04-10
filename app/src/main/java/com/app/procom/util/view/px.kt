package com.app.procom.util.view

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Stable
inline val Int.px
    get() = Px(value = this.toFloat()).dp

@Stable
inline val Int.spx
    get() = Px(value = this.toFloat()).sp

@Stable
inline val Float.spx
    get() = Px(value = this).sp

@JvmInline
value class Px(val value: Float) {

    val dp: Dp
        get() = (value / DENSITY).dp

    val sp: TextUnit
        get() = (value / DENSITY).sp

    companion object {
        private const val DENSITY = 1.5f
    }
}

@JvmInline
value class ComposableText private constructor(val text: String) {
    companion object {
        @Composable
        fun resource(@StringRes id: Int): ComposableText = ComposableText(stringResource(id = id))

        @Composable
        fun resource(@StringRes id: Int, vararg formatArgs: Any): ComposableText =
            ComposableText(stringResource(id = id, *formatArgs))

        fun plain(text: String): ComposableText = ComposableText(text)
    }
}


