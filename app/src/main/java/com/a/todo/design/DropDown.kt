package com.a.todo.design

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch

data class ListDropDown(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)

@Composable
fun CustomDropDown(
    modifier: Modifier = Modifier,
    listDropDown: List<ListDropDown>
) {
    val scope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
        CustomIconButton(
            icon = Icons.Rounded.MoreVert,
            onClick = { isExpanded = true }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            listDropDown.forEach { dropDownMenuItem ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = dropDownMenuItem.icon,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = dropDownMenuItem.title) },
                    onClick = {
                        scope.launch {
                            isExpanded = false
                        }.invokeOnCompletion {
                            dropDownMenuItem.onClick.invoke()
                        }
                    }
                )
            }
        }
    }
}