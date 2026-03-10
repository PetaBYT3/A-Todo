@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Subtitles
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.a.todo.design.CustomComposableElevatedCard
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextField
import com.a.todo.design.innerWindowInsets
import com.a.todo.enumclass.TodoImportance
import com.a.todo.event.EventAddTodo
import com.a.todo.extension.getFutureDateByDaysAsString
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

    LaunchedEffect(viewModel.popBack) {
        viewModel.popBack.collect {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentWindowInsets = innerWindowInsets(),
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
private fun TopBar(
    backStack: NavBackStack<NavKey>
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            CustomIconButton(
                icon = Icons.Rounded.ArrowBack,
                onClick = { backStack.removeAt(backStack.lastIndex) }
            )
        },
        title = {  }
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
        modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CustomComposableElevatedCard(
            modifier = Modifier.padding(horizontal = 15.dp),
            icon = Icons.Rounded.Warning,
            title = "Importance",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                TodoImportance.entries.forEach { importance ->
                    val selected = state.radioButtonTodoImportance == importance

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        onClick = { onEvent(EventAddTodo.RadioButtonTodoImportance(importance)) }
                    ) {
                        CustomTextContent(
                            modifier = Modifier.padding(15.dp),
                            text = importance.value,
                            isSingleLine = true
                        )
                    }
                }
            }
        }
        CustomComposableElevatedCard(
            modifier = Modifier.padding(horizontal = 15.dp),
            icon = Icons.Rounded.DateRange,
            title = "Date"
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    CustomTextContent(
                        text = "${state.textTodoDay} Day From Now",
                        isSingleLine = true
                    )
                    CustomTextContent(
                        text = getFutureDateByDaysAsString(state.textTodoDay),
                        isSingleLine = true
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = { onEvent(EventAddTodo.ButtonDecreaseTodoDay) }
                    ) {
                        Icon(
                            modifier = Modifier.padding(15.dp),
                            imageVector = Icons.Rounded.Remove,
                            contentDescription = null
                        )
                    }
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = { onEvent(EventAddTodo.ButtonIncreaseTodoDay) }
                    ) {
                        Icon(
                            modifier = Modifier.padding(15.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            value = state.textFieldTodoTitle,
            onValueChange = { onEvent(EventAddTodo.TextFieldTodoTitle(it)) },
            leadingIcon = Icons.Rounded.Abc,
            placeholder = "Title"
        )
        CustomTextField(
            modifier = Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 15.dp),
            value = state.textFieldTodoContent,
            onValueChange = { onEvent(EventAddTodo.TextFieldTodoContent(it)) },
            leadingIcon = Icons.Rounded.Subtitles,
            placeholder = "Content"
        )
        Spacer(modifier = Modifier.height(100.dp))
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
        onClick = { onEvent(EventAddTodo.ButtonSaveTodo) },
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