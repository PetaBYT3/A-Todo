package com.a.todo.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CustomTextHeader(
    modifier: Modifier = Modifier,
    text: String,
    isSingleLine: Boolean = false
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE
    )
}

@Composable
fun CustomTextTitle(
    modifier: Modifier = Modifier,
    text: String,
    isSingleLine: Boolean = false
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE
    )
}

@Composable
fun CustomTextContent(
    modifier: Modifier = Modifier,
    text: String,
    isSingleLine: Boolean = false
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        overflow = TextOverflow.Ellipsis,
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE
    )
}