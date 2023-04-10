package com.app.procom.util.view

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.app.procom.R
import com.app.procom.ui.themes.*
import com.app.procom.ui.themes.Dimensions.ARC_FULL_ANGLE
import com.app.procom.ui.themes.Dimensions.ARC_START_ANGLE
import com.app.procom.ui.themes.Dimensions.PADDING_MEDIUM
import kotlin.math.ceil

@Composable
fun AppCircularProgressBar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background.copy(alpha = 0.6f))
            .focusable(true)
            .clickable(enabled = false, onClick = {}),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.px), color = AppTheme.colors.blue.primary)
        AppBodyText(text = stringResource(id = R.string.dialog_loading_body))
    }
}

@Composable
fun AppChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier,

    numVerticalSectors: Int = 4,
    verticalGridLineWidth: Dp = 2.px,
    verticalGridLineAlpha: Float = 0.3f,
    verticalGridLineDashInterval: Dp = 5.px,

    startXAxis: Float = 50f,
    endXAxis: Float = 120f,

    startYAxis: Float = 0f,
    endYAxis: Float = 50f,

    pointRadius: Dp = 2.px,
    lineWidth: Dp = 2.px
) {
    val pointRadiusValue = with(LocalDensity.current) { pointRadius.toPx() }
    val lineWidthValue = with(LocalDensity.current) { lineWidth.toPx() }
    val gridLineWidthValue = with(LocalDensity.current) { verticalGridLineWidth.toPx() }
    val lineDashIntervalValue = with(LocalDensity.current) { verticalGridLineDashInterval.toPx() }

    val verticalGridLineDashIntervals: FloatArray = floatArrayOf(lineDashIntervalValue, lineDashIntervalValue)
    val values = trimHiddenPoints(points, startXAxis, endXAxis)

    Canvas(modifier = modifier.background(LocalAppColors.current.overlay)) {

        val scaleX = size.width / (endXAxis - startXAxis)
        val scaleY = size.height / (endYAxis - startYAxis)

        for (index in values.indices) {
            val point = values[index]
            if (point.isEmpty) {
                values[index] = point
            } else {
                val x = (point.x - startXAxis) * scaleX
                val y = if (endYAxis != startYAxis) {
                    size.height - (point.y - startYAxis) * scaleY
                } else {
                    size.height / 2
                }
                values[index] = ChartPoint(x, y)
            }
        }

        drawFilledPath(values)
        drawLines(values, pointRadiusValue, lineWidthValue)
        drawVerticalGrid(numVerticalSectors, verticalGridLineDashIntervals, gridLineWidthValue, verticalGridLineAlpha)
    }
}

private fun trimHiddenPoints(points: List<ChartPoint>, startX: Float, endX: Float): Array<ChartPoint> {
    var startIndex = 0
    var endIndex: Int = points.lastIndex

    for (index in 1 until points.size) {
        val point = points[index]
        val prevPoint = points[index - 1]
        if (prevPoint.x < startX && point.x >= startX) {
            startIndex = index - 1
        }
        if (prevPoint.x <= endX && point.x > endX) {
            endIndex = index
            break
        }
    }

    val values = Array(endIndex - startIndex + 1) {
        points[it + startIndex]
    }
    return values
}

private fun DrawScope.drawFilledPath(values: Array<ChartPoint>) {
    var minHeight = Float.POSITIVE_INFINITY
    var lastX = 0f
    val path = Path().apply {
        reset()
        for (index in values.indices) {
            val point = values[index]
            val prevPoint = if (index == 0) ChartPoint.empty(point.x) else values[index - 1]
            if (minHeight > point.y) {
                minHeight = point.y
            }
            if (!prevPoint.isEmpty && !point.isEmpty) {
                // draw sub-path
                lastX = point.x
                lineTo(x = point.x, y = point.y)
            } else if (prevPoint.isEmpty && !point.isEmpty) {
                // start sub-path
                lastX = point.x
                moveTo(x = point.x, y = size.height)
                lineTo(x = point.x, y = point.y)
            } else if (!prevPoint.isEmpty) {
                // close sub-path
                lineTo(prevPoint.x, y = size.height)
                close()
            }
        }
        lineTo(x = lastX, y = size.height)
        close()
    }

    val brush = Brush.verticalGradient(
        startY = minHeight,
        colors = listOf(
            Color.White,
            Color.Transparent,
        )
    )
    drawPath(path, brush, style = Fill)
}

private fun DrawScope.drawLines(values: Array<ChartPoint>, pointRadius: Float, lineWidth: Float) {
    for (i in values.indices) {
        val point = values[i]
        val prevPoint = if (i == 0) ChartPoint.empty(point.x) else values[i - 1]
        val nextPoint = if (i == values.size - 1) ChartPoint.empty(point.x) else values[i + 1]

        if (!prevPoint.isEmpty && !point.isEmpty) {
            drawLine(
                color = Color.White,
                start = Offset(prevPoint.x, prevPoint.y),
                end = Offset(point.x, point.y),
                strokeWidth = lineWidth,
                cap = StrokeCap.Round
            )
        } else if (prevPoint.isEmpty && !point.isEmpty && nextPoint.isEmpty) {
            drawCircle(
                color = Color.White,
                radius = pointRadius,
                center = Offset(point.x, point.y),
                style = Fill
            )
        }
    }
}

private fun DrawScope.drawVerticalGrid(numSectors: Int, intervals: FloatArray, lineWidth: Float, alpha: Float) {
    val sectorWidth = size.width / numSectors

    for (i in 1 until numSectors) {
        val x = sectorWidth * i
        val path = verticalDashPath(x, intervals)
        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(lineWidth),
            alpha = alpha,
        )
    }
}

private fun DrawScope.verticalDashPath(x: Float, intervals: FloatArray): Path {
    val numOfSectors = ceil(size.height / intervals.sum()).toInt()

    val dashPath = Path().apply {
        reset()
        for (i in 0..numOfSectors) {
            val startY = i * intervals.sum()
            moveTo(x, startY)
            lineTo(x, startY + intervals[0])
        }
    }
    return dashPath
}

@Composable
fun CircularProgress(
    modifier: Modifier = Modifier,
    progress: Float = 1f,
    strokeWidth: Dp = Dp.Unspecified,
    colors: List<Color> = listOf(appColors.blue.primary, appColors.blue.secondary)
) {
    val brush = Brush.linearGradient(colors)
    val sweepAngle = progress * ARC_FULL_ANGLE
    Canvas(modifier = modifier.padding(PADDING_MEDIUM)) {
        drawArc(
            brush = brush,
            startAngle = ARC_START_ANGLE,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset.Zero,
            style = Stroke(strokeWidth.value)
        )
    }
}

fun Modifier.handleClickEvent(
    enable: Boolean = true,
    onClick: () -> Unit,
    hapticFeedback: () -> Unit
) = this.then(

    this
        .clip(appShapes.small)
        .background(GrayPrimary)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { hapticFeedback.invoke() },
                onLongPress = { hapticFeedback.invoke() },
                onTap = { hapticFeedback.invoke() }
            )
        }
        .clickable(
            onClick = {
                if (enable)
                    onClick()
            }
        )
)

@Composable
fun BoundedRippleEffect(content: @Composable (HapticFeedback) -> Unit) {
    val ripple = rememberRipple(
        color = LocalAppColors.current.red.light,
        bounded = true,
    )
    val haptic = LocalHapticFeedback.current
    CompositionLocalProvider(LocalIndication provides ripple) {
        content(haptic)
    }
}

@Composable
fun ColumnScope.FillPins(modifier: Modifier = Modifier, pinState: String?) {
    pinState?.let {
        if (it.isEmpty()) {
            AppTitleText(
                text = "",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            DotIndicator(
                count = 4,
                modifier = modifier.align(Alignment.CenterHorizontally)
            ) { index ->
                index <= it.length - 1
            }
        }
    }
}

@Composable
fun <T> AppLazyColumn(
    items: List<T>,
    onItemClick: (T) -> Unit,
    key: T.() -> Any,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(T) -> Unit
) {

    @Composable
    fun getShape(index: Int, size: Int) = when (index) {
        0 -> AppTheme.shapes.smallTopRounded
        size - 1 -> AppTheme.shapes.smallBottomRounded
        else -> RoundedCornerShape(ZeroCornerSize)
    }

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.px)) {
        items(count = items.size, key = { items[it].key() }) { index ->
            val shape = getShape(index, items.size)
            AppLazyColumItem(
                item = items[index],
                onItemClick = onItemClick,
                modifier = Modifier.clip(shape = shape),
                content = content
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> LazyItemScope.AppLazyColumItem(
    item: T,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(T) -> Unit
) {
    Box(
        modifier = modifier
            .animateItemPlacement()
            .clickable(onClick = remember { { onItemClick(item) } })
            .background(AppTheme.colors.overlay)
            .defaultMinSize(minHeight = 64.px)
            .fillMaxWidth()
            .padding(8.px)
    ) {
        this.content(item)
    }
}


