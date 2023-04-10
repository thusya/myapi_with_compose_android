package com.app.procom.util.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.app.procom.R
import com.app.procom.ui.themes.AppTheme
import com.app.procom.ui.themes.LocalAppColors
import com.app.procom.util.Constants
import kotlin.math.ceil

@Composable
fun AppBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    defaultXOffset: Dp = (-8).px
) {
    val ripple = rememberRipple(
        color = LocalAppColors.current.gray.light,
        radius = 24.px,
        bounded = false,
    )
    val haptic = LocalHapticFeedback.current
    CompositionLocalProvider(LocalIndication provides ripple) {
        Icon(
            modifier = modifier
                .offset(x = defaultXOffset)
                .size(48.px)
                .clickable(
                    enabled = enabled,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onClick()
                    },
                )
                .padding(4.px),
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = LocalAppColors.current.onBackground
        )
    }
}

@Composable
fun AppRoundedCrewAssistButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,

    borderStrokeWidth: Dp = 3.px,
    innerBorderStrokeWidth: Dp = 3.px,
    cornerRadius: Dp = 60.px,

    backgroundColor: Color = AppTheme.colors.brand.coral,
    pressedBackgroundColor: Color = AppTheme.colors.red.primary,
    contentColor: Color = AppTheme.colors.background,

    animated: Boolean = false,
    animationDuration: Int = 3000,
    onAnimationEnd: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) = AppCrewAssistButton(
    text,
    onClick,
    modifier
        .fillMaxWidth()
        .height(110.px),
    enabled,
    borderStrokeWidth,
    innerBorderStrokeWidth,
    cornerRadius,
    backgroundColor,
    pressedBackgroundColor,
    contentColor,
    animated,
    animationDuration,
    onAnimationEnd,
    interactionSource
)

@Composable
fun AppCircleCrewAssistButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,

    borderStrokeWidth: Dp = 3.px,
    innerBorderStrokeWidth: Dp = 3.px,
    cornerRadius: Dp = 120.px,

    backgroundColor: Color = AppTheme.colors.brand.coral,
    pressedBackgroundColor: Color = AppTheme.colors.red.primary,
    contentColor: Color = AppTheme.colors.background,

    animated: Boolean = false,
    animationDuration: Int = 3000,
    onAnimationEnd: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) = AppCrewAssistButton(
    text,
    onClick,
    modifier.size(240.px),
    enabled,
    borderStrokeWidth,
    innerBorderStrokeWidth,
    cornerRadius,
    backgroundColor,
    pressedBackgroundColor,
    contentColor,
    animated,
    animationDuration,
    onAnimationEnd,
    interactionSource
)

@Composable
private fun AppCrewAssistButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,

    borderStrokeWidth: Dp = 3.px,
    innerBorderStrokeWidth: Dp = 3.px,
    cornerRadius: Dp = 0.px,

    backgroundColor: Color = AppTheme.colors.brand.coral,
    pressedBackgroundColor: Color = AppTheme.colors.red.primary,
    contentColor: Color = AppTheme.colors.background,

    animated: Boolean = false,
    animationDuration: Int = 3000,
    onAnimationEnd: (() -> Unit)? = null,

    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val strokeWidth = with(LocalDensity.current) { borderStrokeWidth.toPx() }
    val innerStrokeWidth = with(LocalDensity.current) { innerBorderStrokeWidth.toPx() }
    val cornerRadiusPx = with(LocalDensity.current) { cornerRadius.toPx() }

    val pressed by interactionSource.collectIsPressedAsState()
    val defaultBackgroundColor = if (pressed) pressedBackgroundColor else backgroundColor

    val progress = if (animated) {
        val forwardAnimSpec = tween<Float>(
            durationMillis = animationDuration,
            easing = LinearEasing
        )
        val reverseAnimSpec = tween<Float>(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        )

        animateFloatAsState(
            targetValue = if (pressed) 0f else 1f,
            animationSpec = if (pressed) forwardAnimSpec else reverseAnimSpec
        ) { progress ->
            if (progress == 0f) {
                onAnimationEnd?.invoke()
            }
        }
    } else {
        remember { mutableStateOf(1f) }
    }
    val secondsToEnd =
        ceil(progress.value * animationDuration / Constants.MILLIS_IN_SEC).toInt().toString()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    enabled = enabled,
                    indication = null,
                    onClick = onClick
                )
        ) {
            drawCrewAssisiButton(
                progress = progress.value,
                strokeWidth = strokeWidth,
                innerStrokeWidth = innerStrokeWidth,
                strokeColor = defaultBackgroundColor,
                innerStrokeColor = contentColor,
                backgroundColor = backgroundColor,
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            )
        }
        Text(
            text = if (pressed && animated) "Hold for $secondsToEnd s" else text,
            style = AppTheme.typography.title
        )
    }
}

fun DrawScope.drawCrewAssisiButton(
    progress: Float = 1f,
    strokeWidth: Float = 0f,
    innerStrokeWidth: Float = 0f,
    strokeColor: Color = Color.Unspecified,
    innerStrokeColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    cornerRadius: CornerRadius = CornerRadius.Zero,
) {
    val size = Size(
        width = size.width * progress - (strokeWidth + innerStrokeWidth) * 2,
        height = size.height * progress - (strokeWidth + innerStrokeWidth) * 2
    )

    val offset = Offset(
        x = (this.size.width - size.width) / 2,
        y = (this.size.height - size.height) / 2
    )

    drawRoundRect(color = strokeColor, cornerRadius = cornerRadius)

    if (size.width >= 0 && size.height >= 0) {
        drawRoundRect(
            color = innerStrokeColor,
            topLeft = offset,
            size = size,
            cornerRadius = cornerRadius,
            style = Stroke(width = innerStrokeWidth * 2)
        )
        drawRoundRect(
            color = backgroundColor,
            topLeft = offset,
            size = size,
            cornerRadius = cornerRadius,
            style = Fill
        )
    }
}

@Composable
fun AppDestructiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    iconSize: Dp = 24.px,
    isShortSized: Boolean = false
) = AppOutlineButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    icon = icon,
    enabled = enabled,
    buttonColors = DefaultAppButtonColors.outlined(
        backgroundColor = AppTheme.colors.background,
        contentColor = AppTheme.colors.red.secondary,
        borderColor = AppTheme.colors.red.primary,
        pressedBackgroundColor = AppTheme.colors.red.primary,
        pressedContentColor = AppTheme.colors.onBackground,
        pressedBorderColor = AppTheme.colors.red.primary,
        disabledBackgroundColor = AppTheme.colors.background,
        disabledContentColor = AppTheme.colors.red.secondary.copy(alpha = Constants.REQUIRED_ALPHA),
        disabledBorderColor = AppTheme.colors.red.primary.copy(alpha = Constants.REQUIRED_ALPHA)
    ),
    interactionSource = interactionSource,
    indication = rememberRipple(color = AppTheme.colors.red.secondary),
    iconSize = iconSize,
    isShortSized = isShortSized
)

@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isShortSized: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    AppButton(
        enabled = enabled,
        buttonColors = DefaultAppButtonColors.filled(
            backgroundColor = AppTheme.colors.overlay,
            contentColor = AppTheme.colors.onBackground,
            disabledBackgroundColor = AppTheme.colors.background,
            disabledContentColor = AppTheme.colors.onBackground.copy(alpha = Constants.REQUIRED_ALPHA)
        ),
        modifier = modifier,
        onClick = onClick,
        isShortSized = isShortSized,
        interactionSource = interactionSource,
        indication = rememberRipple(color = AppTheme.colors.gray.primary)
    ) {
        Text(
            text = text,
            style = AppTheme.typography.title,
        )
    }
}

@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) = AppOutlineButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    icon = icon,
    enabled = enabled,
    buttonColors = DefaultAppButtonColors.outlined(),
    interactionSource = interactionSource,
    indication = rememberRipple(color = AppTheme.colors.blue.primary)
)

@Composable
private fun AppOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
    buttonColors: AppButtonColors = DefaultAppButtonColors.outlined(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    isShortSized: Boolean = false,
    iconSize: Dp = 24.px,
) {
    val pressed = interactionSource.collectIsPressedAsState()
    val borderColor by buttonColors.borderColor(enabled = enabled, pressed = pressed.value)

    AppButton(
        borderStroke = BorderStroke(3.px, borderColor),
        enabled = enabled,
        pressed = pressed.value,
        buttonColors = buttonColors,
        modifier = modifier,
        onClick = onClick,
        interactionSource = interactionSource,
        indication = indication,
        isShortSized = isShortSized
    ) {
        if (icon != null) {
            val padding = if (isShortSized) 0.px else 8.px
            Icon(
                painter = icon,
                modifier = Modifier
                    .padding(start = padding)
                    .size(iconSize)
                    .align(Alignment.CenterStart),
                contentDescription = null // decorative element
            )
        }
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            style = AppTheme.typography.title,
        )
    }
}

@Composable
private fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    pressed: Boolean = false,
    borderStroke: BorderStroke? = null,
    shape: Shape = AppTheme.shapes.small,
    buttonColors: AppButtonColors = DefaultAppButtonColors.outlined(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    isShortSized: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val contentColor by buttonColors.contentColor(enabled, pressed)
    Surface(
        modifier = modifier
            .wrapContentWidth()
            .height(64.px)
            .clickable(interactionSource, indication, enabled) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick.invoke()
            },
        shape = shape,
        border = borderStroke,
        color = buttonColors.backgroundColor(enabled, pressed).value,
        contentColor = contentColor,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(value = AppTheme.typography.title) {
                val boxModifier = if (isShortSized) {
                    Modifier
                        .wrapContentWidth()
                        .padding(start = 40.px, end = 40.px)
                } else {
                    Modifier
                        .fillMaxWidth()
                        .padding(12.px)
                }
                Box(
                    modifier = boxModifier,
                    contentAlignment = Alignment.Center,
                    content = content
                )
            }
        }
    }
}

@Composable
fun AppShortButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
    isShortSized: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) = AppOutlineButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    icon = icon,
    enabled = enabled,
    buttonColors = DefaultAppButtonColors.outlined(),
    interactionSource = interactionSource,
    isShortSized = isShortSized,
    indication = rememberRipple(color = AppTheme.colors.blue.primary)
)

interface AppButtonColors {
    @Composable
    fun backgroundColor(enabled: Boolean, pressed: Boolean): State<Color>

    @Composable
    fun contentColor(enabled: Boolean, pressed: Boolean): State<Color>

    @Composable
    fun borderColor(enabled: Boolean, pressed: Boolean): State<Color>
}

@Immutable
private data class DefaultAppButtonColors(
    private val backgroundColor: Color,
    private val contentColor: Color,
    private val borderColor: Color,
    private val pressedBackgroundColor: Color,
    private val pressedContentColor: Color,
    private val pressedBorderColor: Color,
    private val disabledBackgroundColor: Color,
    private val disabledContentColor: Color,
    private val disabledBorderColor: Color,
) : AppButtonColors {

    @Composable
    override fun backgroundColor(enabled: Boolean, pressed: Boolean): State<Color> {
        val color = when {
            enabled && !pressed -> backgroundColor
            enabled && pressed -> pressedBackgroundColor
            else -> disabledBackgroundColor
        }
        return rememberUpdatedState(color)
    }

    @Composable
    override fun contentColor(enabled: Boolean, pressed: Boolean): State<Color> {
        val color = when {
            enabled && !pressed -> contentColor
            enabled && pressed -> pressedContentColor
            else -> disabledContentColor
        }
        return rememberUpdatedState(color)
    }

    @Composable
    override fun borderColor(enabled: Boolean, pressed: Boolean): State<Color> {
        val color = when {
            enabled && !pressed -> borderColor
            enabled && pressed -> pressedBorderColor
            else -> disabledBorderColor
        }
        return rememberUpdatedState(color)
    }

    companion object {

        @Composable
        fun outlined(
            backgroundColor: Color = AppTheme.colors.background,
            contentColor: Color = AppTheme.colors.blue.secondary,
            borderColor: Color = AppTheme.colors.blue.primary,

            pressedBackgroundColor: Color = AppTheme.colors.blue.primary,
            pressedContentColor: Color = AppTheme.colors.onBackground,
            pressedBorderColor: Color = AppTheme.colors.blue.primary,

            disabledBackgroundColor: Color = AppTheme.colors.background,
            disabledContentColor: Color = AppTheme.colors.blue.secondary.copy(alpha = Constants.REQUIRED_ALPHA),
            disabledBorderColor: Color = AppTheme.colors.blue.primary.copy(alpha = Constants.REQUIRED_ALPHA),
        ): AppButtonColors = DefaultAppButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            borderColor = borderColor,
            pressedBackgroundColor = pressedBackgroundColor,
            pressedContentColor = pressedContentColor,
            pressedBorderColor = pressedBorderColor,
            disabledBackgroundColor = disabledBackgroundColor,
            disabledContentColor = disabledContentColor,
            disabledBorderColor = disabledBorderColor,
        )

        @Composable
        fun filled(
            backgroundColor: Color = AppTheme.colors.background,
            pressedBackgroundColor: Color = AppTheme.colors.blue.secondary,
            disabledBackgroundColor: Color = AppTheme.colors.background,
            contentColor: Color = AppTheme.colors.blue.secondary,
            pressedContentColor: Color = AppTheme.colors.onBackground,
            disabledContentColor: Color = AppTheme.colors.blue.secondary.copy(alpha = Constants.REQUIRED_ALPHA),
        ): AppButtonColors = DefaultAppButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            borderColor = backgroundColor,
            pressedBackgroundColor = pressedBackgroundColor,
            pressedContentColor = pressedContentColor,
            pressedBorderColor = pressedBackgroundColor,
            disabledBackgroundColor = disabledBackgroundColor,
            disabledContentColor = disabledContentColor,
            disabledBorderColor = disabledBackgroundColor,
        )
    }
}

@Composable
fun AppBackSpaceIcon(
    modifier: Modifier = Modifier,
    drawablePath: Int,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val button: Painter = painterResource(id = drawablePath)
    Box {
        Image(
            painter = button,
            contentDescription = null,
            modifier = modifier
                .width(88.px)
                .height(72.px)
                .padding(start = 20.px, end = 20.px, top = 4.px)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { onClick() },
                        onLongPress = { onLongClick() }
                    )
                }

        )
    }
}

@Preview
@Composable
fun PreviewPrimaryButton() {
    AppTheme {
        Column {
            AppPrimaryButton(
                "Primary Enabled",
                onClick = {},
                enabled = true,
            )
            Spacer(modifier = Modifier.height(8.px))
            AppPrimaryButton(
                "Primary with icon",
                onClick = {},
                enabled = true,
                icon = painterResource(id = R.drawable.ic_settings),
            )
            Spacer(modifier = Modifier.height(8.px))
            AppPrimaryButton(
                "Primary Disabled",
                onClick = {},
                enabled = false,
                icon = painterResource(id = R.drawable.ic_settings),
            )
        }
    }
}

@Preview
@Composable
fun PreviewSecondaryButton() {
    AppTheme {
        Column {
            AppSecondaryButton(
                "Secondary Enabled",
                onClick = {},
                enabled = true,
            )
            Spacer(modifier = Modifier.height(8.px))
            AppSecondaryButton(
                "Secondary Disabled",
                onClick = {},
                enabled = false,
            )
        }
    }
}

@Preview
@Composable
fun PreviewDistructiveButton() {
    AppTheme {
        Column {
            AppDestructiveButton(
                "Distructive Enabled",
                onClick = {},
                enabled = true,
            )
            Spacer(modifier = Modifier.height(8.px))
            AppDestructiveButton(
                "Distructive Disabled",
                onClick = {},
                enabled = false,
            )
        }
    }
}

@Preview
@Composable
fun PreviewCrewAssistButton() {
    AppTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppRoundedCrewAssistButton(
                onClick = {},
                text = "Crew Assist",
                animated = false
            )
            Spacer(modifier = Modifier.height(8.px))
            AppCircleCrewAssistButton(
                onClick = {},
                text = "Crew Assist",
                animated = true
            )
        }
    }
}

@Preview
@Composable
fun PreviewBackButton() {
    AppTheme {
        AppBackButton(onClick = {})
    }
}


@Preview
@Composable
fun AppShortButtonWithTextPreview() {
    AppTheme {
        AppShortButton(
            text = "1",
            onClick = {},
            enabled = true,
            isShortSized = true
        )
    }
}


