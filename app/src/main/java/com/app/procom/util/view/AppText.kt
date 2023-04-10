@file:Suppress("LongParameterList")

package com.app.procom.util.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.app.procom.ui.themes.AppTheme

@Composable
fun AppTitleText(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = AppTheme.typography.title.fontWeight,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = AppTheme.colors.onBackground,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        style = AppTheme.typography.title,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun AppBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.onBackground,
    fontWeight: FontWeight? = AppTheme.typography.body.fontWeight,
    style: TextStyle = AppTheme.typography.body,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        style = style,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun AppCaptionText(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = AppTheme.typography.caption.fontWeight,
    style: TextStyle = AppTheme.typography.caption,
    color: Color = AppTheme.colors.gray.primary,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        style = style,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun AppLinkText(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = AppTheme.typography.title.fontWeight,
    fontSize: TextUnit = TextUnit.Unspecified,
    onClick: (Int) -> Unit = {}
) {
    ClickableText(
        text = AnnotatedString(text),
        onClick = onClick,
        style = TextStyle(
            color = AppTheme.colors.blue.primary,
            fontWeight = fontWeight,
            fontSize = fontSize
        ),
        modifier = modifier.wrapContentSize()
    )
}

@Composable
fun AppBodyAnnotatedText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.onBackground,
    fontWeight: FontWeight? = AppTheme.typography.body.fontWeight,
    style: TextStyle = AppTheme.typography.body,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        style = style,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
)
@Composable
fun PreviewTitle() {
    AppTheme {
        Column {
            AppTitleText(text = "Title text")
            AppBodyText(text = "Body text")
            AppCaptionText(text = "Caption")
            AppLinkText(text = "Link")
        }
    }
}


