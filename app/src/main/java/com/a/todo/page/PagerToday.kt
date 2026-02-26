@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.todo.design.CustomOutlinedButton
import com.a.todo.design.CustomSingleButtonGroup
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextHeader
import com.a.todo.design.CustomTextTitle
import com.a.todo.event.EventToday
import com.a.todo.repository.ResponseDatabase
import com.a.todo.state.StateToday
import com.a.todo.viewmodel.ViewModelToday
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Preview(showBackground = true)
@Composable
fun PagerToday(
    viewModel: ViewModelToday = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val buttonList = listOf("Todo", "Done")
        var buttonGroupState by rememberSaveable { mutableStateOf(buttonList[0]) }
        val pagerState = rememberPagerState(pageCount = { buttonList.size })
        CustomSingleButtonGroup(
            modifier = Modifier.fillMaxWidth(),
            buttonList = buttonList,
            value = buttonGroupState,
            onCheckedChange = {
                buttonGroupState = it
                scope.launch {
                    pagerState.animateScrollToPage(buttonList.indexOf(it))
                }
            }
        )
        LaunchedEffect(pagerState.targetPage) {
            buttonGroupState = buttonList[pagerState.targetPage]
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { pager ->
            when (pager) {
                0 -> PagerTodo(
                    state = state,
                    onEvent = onEvent
                )
                1 -> PagerDone(
                    state = state
                )
            }
        }
    }
}

@Composable
private fun PagerTodo(
    state: StateToday,
    onEvent: (EventToday) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.todoTodoTodayResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            is ResponseDatabase.Success -> {
                if (state.todoTodoTodayResponse.listTodo.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(
                            items = state.todoTodoTodayResponse.listTodo
                        ) { todoToday ->
                            Card(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
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
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            CustomTextTitle(text = todoToday.todoTitle)
                                            CustomTextTitle(text = "Date")
                                        }
                                        CustomTextContent(text = todoToday.todoContent)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    CustomOutlinedButton(
                                        text = "Done",
                                        onClick = { onEvent(EventToday.ButtonMarkAsDone(todoToday)) }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    CustomTextHeader(
                        modifier = Modifier.align(Alignment.TopCenter),
                        text = "Nothing to do today"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = state.todoTodoTodayResponse.messageFailed
                )
            }
        }
    }
}

@Composable
private fun PagerDone(
    state: StateToday
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.doneTodoTodayResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            is ResponseDatabase.Success -> {
                if (state.doneTodoTodayResponse.listTodo.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(
                            items = state.doneTodoTodayResponse.listTodo
                        ) { todoToday ->
                            Card(
                                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
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
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            CustomTextTitle(text = todoToday.todoTitle)
                                            CustomTextTitle(text = "Date")
                                        }
                                        CustomTextContent(text = todoToday.todoContent)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                } else {
                    CustomTextHeader(
                        modifier = Modifier.align(Alignment.TopCenter),
                        text = "Not any task done yet"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = state.doneTodoTodayResponse.messageFailed
                )
            }
        }
    }
}