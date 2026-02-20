package com.a.todo.design

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomBoxCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = modifier.padding(15.dp)
        ) {
            content()
        }
    }
}

@Composable
fun CustomColumnCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = modifier.padding(15.dp)
        ) {
            content()
        }
    }
}

@Composable
fun CustomRowCard(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Card(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = modifier.padding(15.dp)
        ) {
            content()
        }
    }
}