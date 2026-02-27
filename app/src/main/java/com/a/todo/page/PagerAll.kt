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
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.Dangerous
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Task
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomSingleButtonGroup
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextHeader
import com.a.todo.design.CustomTextTitle
import com.a.todo.extension.convertLongToString
import com.a.todo.repository.ResponseDatabase
import com.a.todo.state.StateAll
import com.a.todo.viewmodel.ViewModelAll
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PagerAll(
    viewModel: ViewModelAll = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val buttonList = listOf("Todo", "Done", "Expired")
        var buttonGroupState by rememberSaveable { mutableStateOf(buttonList[0]) }
        val pagerState = rememberPagerState(pageCount = { buttonList.size })
        CustomSingleButtonGroup(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
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
            state = pagerState
        ) { pager ->
            when (pager) {
                0 -> PagerAllTodo(
                    state = state
                )
                1 -> PagerAllDone(
                    state = state
                )
                2 -> PagerAllExpired(
                    state = state
                )
            }
        }
    }
}

@Composable
private fun PagerAllTodo(
    state: StateAll
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.todoAllTodoResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            is ResponseDatabase.Success -> {
                if (state.todoAllTodoResponse.listTodo.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(
                            items = state.todoAllTodoResponse.listTodo
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
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            CustomTextTitle(text = todoToday.todoTitle)
                                            CustomTextContent(text = convertLongToString(todoToday.todoDate))
                                        }
                                        CustomTextContent(text = todoToday.todoContent)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    CustomIconButton(
                                        icon = Icons.Rounded.Schedule,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }
                } else {
                    CustomTextHeader(
                        modifier = Modifier.align(Alignment.TopCenter),
                        text = "Not a single task have been added"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = state.todoAllTodoResponse.messageFailed
                )
            }
        }
    }
}

@Composable
private fun PagerAllDone(
    state: StateAll
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.todoAllDoneResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            is ResponseDatabase.Success -> {
                if (state.todoAllDoneResponse.listTodo.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(
                            items = state.todoAllDoneResponse.listTodo
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
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            CustomTextTitle(text = todoToday.todoTitle)
                                            CustomTextContent(text = convertLongToString(todoToday.todoDate))
                                        }
                                        CustomTextContent(text = todoToday.todoContent)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
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
                        text = "Not a single task done"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = state.todoAllDoneResponse.messageFailed
                )
            }
        }
    }
}

@Composable
private fun PagerAllExpired(
    state: StateAll
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.todoAllExpiredResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            is ResponseDatabase.Success -> {
                if (state.todoAllExpiredResponse.listTodo.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(
                            items = state.todoAllExpiredResponse.listTodo
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
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            CustomTextTitle(text = todoToday.todoTitle)
                                            CustomTextContent(text = convertLongToString(todoToday.todoDate))
                                        }
                                        CustomTextContent(text = todoToday.todoContent)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    CustomIconButton(
                                        icon = Icons.Rounded.Dangerous,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }
                } else {
                    CustomTextHeader(
                        modifier = Modifier.align(Alignment.TopCenter),
                        text = "Not a single task missed"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = state.todoAllExpiredResponse.messageFailed
                )
            }
        }
    }
}