@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.a.todo.design

import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomSingleButtonGroup(
    modifier: Modifier = Modifier,
    buttonList: List<String>,
    value: String,
    onCheckedChange: (String) -> Unit
) {

    ButtonGroup(
        modifier = modifier,
        overflowIndicator = {}
    ) {
        buttonList.forEach { buttonItem ->
            this@ButtonGroup.toggleableItem(
                weight = 1f,
                checked = value == buttonItem,
                label = buttonItem,
                onCheckedChange = { onCheckedChange.invoke(buttonItem) }
            )
        }
    }
}