package com.app.procom.util.view

import androidx.annotation.IntRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.app.procom.ui.themes.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun <T : Any> AppPager(
    items: List<T>,
    modifier: Modifier = Modifier,
    state: PagerState = rememberAppPagerState(),
    scrollableState: ScrollableState = rememberScrollableState { it },
    contentFactory: @Composable (T) -> Unit,
) {
    state.numberOfItems = remember { items.size }
    state.scope = rememberCoroutineScope()

    Layout(
        content = {
            items.map { item ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    contentFactory(item)
                }
            }
        },
        modifier = modifier
            .scrollable(scrollableState, orientation = Orientation.Horizontal)
            .clipToBounds()
            .then(state.inputModifier),
    ) { measurables: List<Measurable>, constraints: Constraints ->
        val dimension = constraints.maxWidth
        val placeables = measurables.map { measurable -> measurable.measure(constraints) }
        val size = placeables.getSize(dimension)
        state.updateDimension(dimension)
        layout(size.width, size.height) {
            val dragOffset = state.dragOffset.value
            val roundedDragOffset = dragOffset.roundToInt()
            val first = ceil(
                (dragOffset - dimension) / dimension
            ).toInt().coerceAtLeast(0)
            val last = ((dimension + dragOffset) / dimension).toInt()
                .coerceAtMost(items.lastIndex)
            for (i in first..last) {
                val offset = i * (dimension) - roundedDragOffset
                placeables[i].place(x = offset, y = 0)
            }
        }
    }
}

private fun List<Placeable>.getSize(dimension: Int): IntSize {
    return IntSize(dimension, maxByOrNull { it.height }?.height ?: 0)
}

private val PAGER_INDICATOR_DOT_SIZE = 6.px
private val PAGER_INDICATOR_SPACE = 16.px

@Composable
fun AppPagerIndicator(
    state: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = AppTheme.colors.red.primary,
    inactiveColor: Color = AppTheme.colors.onBackground,
    indicatorRadius: Dp = PAGER_INDICATOR_DOT_SIZE,
    spacing: Dp = PAGER_INDICATOR_SPACE,
) {
    val numberItems = state.numberOfItems
    val progress = state.progress
    val indicatorRadiusPx = with(LocalDensity.current) { indicatorRadius.toPx() }
    val spacingPx = with(LocalDensity.current) { spacing.toPx() }

    Canvas(modifier = modifier) {
        val totalWidth = numberItems * 2 * indicatorRadiusPx + (numberItems - 1) * spacingPx
        val initialXOffset = indicatorRadiusPx + (size.width - totalWidth) / 2
        val initialYOffset = size.height / 2
        repeat(numberItems) { index ->
            val sign = if (progress > index.toFloat() / (numberItems - 1)) -1 else 1
            val colorProgress =
                (sign * ((numberItems - 1) * progress - (index - sign))).coerceIn(0f, 1f)
            val color = lerp(inactiveColor, activeColor, colorProgress)
            drawCircle(
                color,
                radius = indicatorRadiusPx,
                center = Offset(
                    x = initialXOffset + index * (2 * indicatorRadiusPx + spacingPx),
                    y = initialYOffset
                )
            )
        }
    }
}

@Composable
fun rememberAppPagerState(@IntRange(from = 0) initialPage: Int = 0): PagerState =
    rememberSaveable(saver = PagerState.Saver) { PagerState(initialPage) }

class PagerState(initialIndex: Int = 0, initialProgress: Float = 0f) {

    var numberOfItems by mutableStateOf(0)
    var scope: CoroutineScope? by mutableStateOf(null)
    var progress: Float by mutableStateOf(initialProgress)
        private set

    var currentPage by mutableStateOf(initialIndex)
        private set

    lateinit var dragOffset: Animatable<Float, AnimationVector1D>
        private set

    private var itemDimension by mutableStateOf(0)
    private val tracker = VelocityTracker()
    private val animationSpec = TweenSpec<Float>(durationMillis = ANIMATION_DURATION)
    private val availableOffsetWidth get() = ((numberOfItems - 1) * itemDimension).toFloat()

    fun updateDimension(dimension: Int) {
        if (this.itemDimension == dimension && dimension != 0) return
        this.itemDimension = dimension
        val initialOffset = currentPage * dimension.toFloat()
        updateProgress(initialOffset)
        dragOffset = Animatable(initialOffset)
        scope?.launch {
            dragOffset.snapTo(currentPage.toFloat() * dimension)
        }
    }

    private fun itemIndex(offset: Int): Int =
        (offset / itemDimension).coerceIn(0, numberOfItems - 1)

    private fun updateIndex(offset: Float) {
        updateProgress(offset)
        val index = itemIndex(offset.roundToInt())
        if (index != currentPage) {
            currentPage = index
        }
    }

    val inputModifier = Modifier.pointerInput(numberOfItems) {
        val decay = splineBasedDecay<Float>(this)
        detectHorizontalDragGestures(
            onHorizontalDrag = { change: PointerInputChange, dragAmount: Float ->
                scope?.launch {
                    val offset = (dragOffset.value - dragAmount).coerceIn(0f, availableOffsetWidth)
                    updateProgress(offset)
                    dragOffset.snapTo(offset)
                }
                tracker.addPosition(change.uptimeMillis, change.position)
            },
            onDragEnd = {
                scope?.launch {
                    val velocity = tracker.calculateVelocity().x
                    val targetOffset = decay.calculateTargetValue(dragOffset.value, -velocity)
                        .coerceIn(0f, availableOffsetWidth)

                    val remainder = targetOffset.toInt().absoluteValue % itemDimension
                    val extra = if (remainder > itemDimension / 2f) 1 else 0

                    val skipItem =
                        ((targetOffset.absoluteValue / itemDimension.toFloat()).toInt() + extra)
                            .coerceIn(currentPage - 1, currentPage + 1)

                    val targetOffsetFixed = skipItem * itemDimension.toFloat()
                        .coerceIn(0f, availableOffsetWidth)
                    dragOffset.animateTo(
                        animationSpec = animationSpec,
                        targetValue = targetOffsetFixed,
                        initialVelocity = -velocity
                    ) {
                        updateIndex(value)
                    }
                }
            },
            onDragStart = {
                tracker.resetTracking()
                scope?.launch { dragOffset.stop() }
            }
        )
    }

    suspend fun animateScrollToPage(page: Int) {
        dragOffset.animateTo(
            animationSpec = animationSpec,
            targetValue = (page * itemDimension).toFloat(),
            initialVelocity = 0f
        ) {
            updateIndex(value)
        }
    }

    private fun updateProgress(offset: Float) {
        progress = (offset / availableOffsetWidth).coerceIn(0f, 1f)
    }

    companion object {
        val Saver: Saver<PagerState, *> = listSaver(save = {
            listOf<Any>(it.currentPage, it.progress)
        }, restore = {
            PagerState(initialIndex = it[0] as Int, initialProgress = it[1] as Float)
        })

        private const val ANIMATION_DURATION: Int = 200
    }
}


