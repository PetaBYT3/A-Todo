@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.a.todo.design

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    Button(
        modifier = modifier.height(50.dp),
        onClick = { onClick.invoke() },
        enabled = !isLoading
    ) {
        when {
            isLoading -> {
                LoadingIndicator()
            }
            else -> {
                Text(text = text)
            }
        }
    }
}

@Composable
fun CustomOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    OutlinedButton(
        modifier = modifier.height(50.dp),
        onClick = { onClick.invoke() },
        enabled = !isLoading
    ) {
        when {
            isLoading -> {
                LoadingIndicator()
            }
            else -> {
                Text(text = text)
            }
        }
    }
}

@Composable
fun CustomIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick.invoke() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}