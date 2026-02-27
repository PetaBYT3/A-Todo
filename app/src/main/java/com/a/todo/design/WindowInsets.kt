package com.a.todo.design

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun rootWindowInsets(): WindowInsets {
    return WindowInsets.systemBars.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
    )
}

@Composable
fun innerWindowInsets(): WindowInsets {
    return WindowInsets.systemBars.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Top
    )
}