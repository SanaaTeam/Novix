package com.sanaa.presentation.shared_component

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.text.ClickableText // Import ClickableText

@Composable
fun OverviewSection(
    onReadMore: () -> Unit,
    @StringRes titleResId: Int,
    overview: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 4,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = titleResId),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title
        )

        Spacer(Modifier.height(8.dp))

        ExpandableText(
            text = overview,
            style = Theme.textStyle.body.medium,
            color = Theme.colors.body,
            collapsedMaxLines = collapsedMaxLines,
            readMoreText = " ${stringResource(R.string.read_more)}",
            readLessText = " ${stringResource(R.string.read_less)}",
            onReadMore = onReadMore
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    color: Color,
    collapsedMaxLines: Int = 4,
    readMoreText: String,
    readLessText: String,
    onReadMore: (() -> Unit)? = null
) {
    val textMeasurer = rememberTextMeasurer()

    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var hasOverflow by rememberSaveable { mutableStateOf(false) }
    var displayText by rememberSaveable { mutableStateOf(text) }

    val READ_MORE_TAG = "read_more_tag"
    val READ_LESS_TAG = "read_less_tag"

    BoxWithConstraints(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 150,
                    easing = FastOutSlowInEasing
                )
            )
    ) {
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }

        LaunchedEffect(text, isExpanded, maxWidthPx) {
            val layoutResult: TextLayoutResult = textMeasurer.measure(
                text = AnnotatedString(text),
                style = style,
                constraints = Constraints(maxWidth = maxWidthPx.toInt())
            )

            if (layoutResult.lineCount > collapsedMaxLines) {
                hasOverflow = true

                if (!isExpanded) {
                    val lastChar = layoutResult
                        .getLineEnd(collapsedMaxLines - 1, visibleEnd = true)

                    val rawCutoff = (lastChar - readMoreText.length)
                        .coerceAtLeast(0)

                    val raw = text.substring(0, rawCutoff)
                    val lastSpace = raw.lastIndexOf(' ')
                    val safe = if (lastSpace != -1) raw.substring(0, lastSpace) else raw

                    displayText = safe.trimEnd() + readMoreText
                } else {
                    displayText = text + readLessText
                }
            } else {
                hasOverflow = false
                displayText = text
            }
        }

        val annotatedString = buildAnnotatedString {
            when {
                !isExpanded && hasOverflow && displayText.endsWith(readMoreText) -> {
                    append(displayText.removeSuffix(readMoreText))
                    withStyle(
                        Theme.textStyle.label.medium
                            .toSpanStyle()
                            .copy(color = Theme.colors.primary)
                    ) {
                        pushStringAnnotation(tag = READ_MORE_TAG, annotation = "read_more")
                        append(readMoreText)
                        pop()
                    }
                }
                isExpanded && hasOverflow && displayText.endsWith(readLessText) -> {
                    append(displayText.removeSuffix(readLessText))
                    withStyle(
                        Theme.textStyle.label.medium
                            .toSpanStyle()
                            .copy(color = Theme.colors.primary)
                    ) {
                        pushStringAnnotation(tag = READ_LESS_TAG, annotation = "read_less")
                        append(readLessText)
                        pop()
                    }
                }
                else -> append(displayText)
            }
        }

        ClickableText(
            text = annotatedString,
            style = style.copy(color = color),
            maxLines = if (!isExpanded && hasOverflow) collapsedMaxLines else Int.MAX_VALUE,
            overflow = TextOverflow.Clip,
            onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset)
                    .firstOrNull()?.let { span ->
                        if (span.tag == READ_MORE_TAG || span.tag == READ_LESS_TAG) {
                            isExpanded = !isExpanded
                            if (isExpanded) onReadMore?.invoke()
                        }
                    }
            }
        )
    }
}