package com.app.procom.util.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.app.procom.ui.themes.LocalAppColors
import com.app.procom.ui.themes.LocalAppShapes

@Composable
fun AppInfoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(LocalAppShapes.current.small)
            .defaultMinSize(minWidth = 164.px, minHeight = 88.px)
            .background(color = LocalAppColors.current.overlay)
            .padding(12.px),
        verticalArrangement = Arrangement.Center,
        content = content,
    )
}


