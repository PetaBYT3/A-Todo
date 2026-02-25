@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Subtitles
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomSingleButtonGroup
import com.a.todo.design.CustomTextField
import com.a.todo.design.CustomTextHeader
import com.a.todo.design.CustomTextTitle
import com.a.todo.design.CustomVerticalCardGroup
import com.a.todo.design.InnerWindowInsets
import com.a.todo.event.EventAddTodo
import com.a.todo.state.StateAddTodo
import com.a.todo.viewmodel.ViewModelAddTodo
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageAddTodo(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelAddTodo = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentWindowInsets = InnerWindowInsets,
        topBar = {
            TopBar(
                backStack = backStack
            )
        },
        content = { innerPadding ->
            Content(
                innerPadding = innerPadding,
                backStack = backStack,
                state = state,
                onEvent = onEvent
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onEvent = onEvent
            )
        }
    )
}

@Composable
private fun TopBar(backStack: NavBackStack<NavKey>) {
    LargeTopAppBar(
        navigationIcon = {
            CustomIconButton(
                icon = Icons.Rounded.ArrowBack,
                onClick = { backStack.removeAt(backStack.lastIndex) }
            )
        },
        title = { Text(text = "Add Todo") }
    )
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    backStack: NavBackStack<NavKey>,
    state: StateAddTodo,
    onEvent: (EventAddTodo) -> Unit
) {
    Column(
        modifier = Modifier.padding(innerPadding).padding(horizontal = 15.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CustomTextHeader(text = "Importance")
            val buttonList = listOf("Low", "Medium", "High")
            CustomSingleButtonGroup(
                buttonList = buttonList,
                value = state.buttonGroupTodoImportance,
                onCheckedChange = { onEvent(EventAddTodo.ButtonGroupTodoImportance(it)) }
            )
        }
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.textFieldTodoTitle,
            onValueChange = { onEvent(EventAddTodo.TextFieldTodoTitle(it)) },
            leadingIcon = Icons.Rounded.Abc,
            placeholder = "Title"
        )
        CustomTextField(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            value = state.textFieldTodoContent,
            onValueChange = { onEvent(EventAddTodo.TextFieldTodoContent(it)) },
            leadingIcon = Icons.Rounded.Subtitles,
            placeholder = "Content"
        )
    }
}

@Composable
private fun FloatingActionButton(
    onEvent: (EventAddTodo) -> Unit
) {
    ExtendedFloatingActionButton(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Save,
                contentDescription = null
            )
        },
        text = { Text(text = "Save") },
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Content(
        innerPadding = PaddingValues(0.dp),
        backStack = rememberNavBackStack(),
        state = StateAddTodo(),
        onEvent = {}
    )
}