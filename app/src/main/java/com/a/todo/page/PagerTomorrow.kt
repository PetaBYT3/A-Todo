@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextHeader
import com.a.todo.design.CustomTextTitle
import com.a.todo.extension.convertLongToString
import com.a.todo.repository.ResponseDatabase
import com.a.todo.state.StateTomorrow
import com.a.todo.viewmodel.ViewModelTomorrow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PagerTomorrow(
    viewModel: ViewModelTomorrow = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PagerTodoTomorrow(
        state = state
    )
}

@Composable
private fun PagerTodoTomorrow(
    state: StateTomorrow
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.todoTomorrowResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            is ResponseDatabase.Success -> {
                if (state.todoTomorrowResponse.listTodo.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(
                            items = state.todoTomorrowResponse.listTodo
                        ) { todoToday ->
                            Card(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(horizontal = 15.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(15.dp),
                                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Warning,
                                        contentDescription = null,
                                        tint = when (todoToday.todoImportance) {
                                            "Low" -> Color.Green
                                            "Medium" -> Color.Yellow
                                            "High" -> Color.Red
                                            else -> Color.Unspecified
                                        }
                                    )
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            CustomTextTitle(text = todoToday.todoTitle)
                                            CustomTextContent(text = convertLongToString(todoToday.todoDate))
                                        }
                                        CustomTextContent(
                                            text = todoToday.todoContent,
                                            isSingleLine = true
                                        )
                                    }
                                    CustomIconButton(
                                        icon = Icons.Rounded.CheckBox,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }
                } else {
                    CustomTextHeader(
                        modifier = Modifier.align(Alignment.TopCenter),
                        text = "Nothing to do tomorrow"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = state.todoTomorrowResponse.messageFailed
                )
            }
        }
    }
}