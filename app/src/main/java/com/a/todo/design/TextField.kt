@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.a.todo.design

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    leadingIcon: ImageVector,
    placeholder: String,
    trailingIcon: ImageVector? = null,
    onClickTrailing: (() -> Unit)? = null
) {
    MaterialTheme(
        motionScheme = MotionScheme.standard()
    ) {
        ElevatedCard(
            modifier = modifier.height(IntrinsicSize.Min)
        ) {
            TextField(
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                value = value,
                onValueChange = { onValueChange(it) },
                enabled = enabled,
                placeholder = { CustomTextContent(text = placeholder) },
                trailingIcon = {
                    if (trailingIcon != null) {
                        IconButton(
                            onClick = { onClickTrailing?.invoke() }
                        ) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CustomPasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var isPasswordVisible by remember { mutableStateOf(true) }

    MaterialTheme(
        motionScheme = MotionScheme.standard()
    ) {
        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = { onValueChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Password,
                    contentDescription = null
                )
            },
            placeholder = { Text(text = placeholder) },
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        )
    }
}