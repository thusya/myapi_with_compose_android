package com.app.procom.util.view

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.app.procom.ui.themes.AppTheme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
    textStyle: TextStyle = AppTheme.typography.title.copy(textAlign = TextAlign.Center),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    shape: Shape = AppTheme.shapes.small,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = AppTheme.colors.background,
        focusedBorderColor = AppTheme.colors.blue.dark,
        unfocusedBorderColor = AppTheme.colors.blue.primary,
        textColor = AppTheme.colors.blue.secondary,
        cursorColor = AppTheme.colors.blue.secondary
    )

    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    @OptIn(ExperimentalMaterialApi::class)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(colors.backgroundColor(enabled).value, shape)
            .fillMaxWidth()
            .height(64.px),
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        textStyle = mergedTextStyle,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = enabled,
                isError = isError,
                singleLine = singleLine,
                colors = colors,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.px),
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled,
                        isError,
                        interactionSource,
                        colors,
                        shape,
                        focusedBorderThickness = 3.px,
                        unfocusedBorderThickness = 3.px
                    )
                }
            )
        }
    )
}

@Preview
@Composable
fun AppTextFieldPreview() {
    AppTheme {
        AppTextField(value = "Text", onValueChange = {})
    }
}


