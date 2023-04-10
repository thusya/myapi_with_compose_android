@file:Suppress("MagicNumber")

package com.app.procom.util.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.app.procom.ui.themes.AppTheme

@Suppress("LongParameterList")
@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    count: Int = 0,
    dotRadius: Dp = 16.px,
    dotSpacing: Dp = 4.px,
    dotColor: Color = White,
    selectedDotColor: Color = Red,
    dotSelectedFunc: (Int) -> Boolean = { false }
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .size(dotRadius)
                    .clip(CircleShape)
                    .background(if (dotSelectedFunc(index)) selectedDotColor else dotColor)
            )
            if (index != count - 1) {
                Spacer(modifier = Modifier.width(dotSpacing))
            }
        }
    }
}

@Preview
@Composable
fun DefaultDotIndicatorPreview() {
    AppTheme {
        DotIndicator(count = 4)
    }
}

@Preview
@Composable
fun DotIndicatorForViewPagerPreview() {
    AppTheme {
        DotIndicator(count = 4) {
            it == 3
        }
    }
}

@Preview
@Composable
fun DotIndicatorForLoginPreview() {
    AppTheme {
        DotIndicator(count = 7) {
            it <= 2
        }
    }
}


