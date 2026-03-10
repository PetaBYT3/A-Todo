@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.design.CustomComposableBottomSheet
import com.a.todo.design.CustomComposableElevatedCard
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextHeader
import com.a.todo.design.innerWindowInsets
import com.a.todo.event.EventToday
import com.a.todo.extension.convertLongToString
import com.a.todo.navigation.RoutePage
import com.a.todo.repository.ResponseDatabase
import com.a.todo.state.StateToday
import com.a.todo.viewmodel.ViewModelToday
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageToday(
    backStack: NavBackStack<NavKey>,
    drawerState: DrawerState,
    viewModel: ViewModelToday = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.imePadding().nestedScroll(scrollBehaviour.nestedScrollConnection),
        contentWindowInsets = innerWindowInsets(),
        topBar = {
            TopBar(
                scrollBehavior = scrollBehaviour,
                onNavigationClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        },
        content = { innerPadding ->
            Content(
                innerPadding = innerPadding,
                state = state,
                onEvent = onEvent
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { backStack.add(RoutePage.PageAddTodo) }
            )
        }
    )

    CustomComposableBottomSheet(
        isBottomSheetVisible = state.bottomSheetMarkAsDone,
        title = "Mark Todo as Done",
        content = {
            CustomComposableElevatedCard(
                modifier = Modifier,
                icon = Icons.Rounded.CheckBoxOutlineBlank,
                title = state.todoToDelete?.todoTitle ?: "",
                onClick = {}
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    CustomTextContent(
                        text = state.todoToDelete?.todoImportance ?: "",
                        isSingleLine = true
                    )
                    CustomTextContent(
                        text = convertLongToString(state.todoToDelete?.todoDate ?: 0),
                        isSingleLine = true
                    )
                    CustomTextContent(
                        text = state.todoToDelete?.todoContent ?: "",
                        isSingleLine = true
                    )
                }
            }
        },
        onCancel = {
            onEvent(EventToday.BottomSheetMarkAsDoneVisibility(false, null))
        },
        onConfirm = {
            onEvent(EventToday.ButtonMarkAsDone)
            onEvent(EventToday.BottomSheetMarkAsDoneVisibility(false, null))
        }
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationClick: () -> Unit
) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            CustomIconButton(
                icon = Icons.Rounded.Menu,
                onClick = { onNavigationClick.invoke() }
            )
        },
        title = { Text(text = "Today") },
        scrollBehavior = scrollBehavior
    )
}

private enum class TabToday(
    val icon: ImageVector,
    val title: String
) {
    Todo(Icons.Rounded.CheckBoxOutlineBlank, "Todo"),
    Done(Icons.Rounded.CheckBox, "Done")
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateToday,
    onEvent: (EventToday) -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val tabs = TabToday.entries
        val pagerState = rememberPagerState(
            pageCount = { tabs.size }
        )
        ElevatedCard(
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            PrimaryTabRow(
                containerColor = Color.Transparent,
                selectedTabIndex = pagerState.currentPage,
                divider = {}
            ) {
                tabs.forEach { tab ->
                    Tab(
                        text = { Text(text = tab.title) },
                        selected = pagerState.currentPage == tab.ordinal,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(tab.ordinal)
                            }
                        }
                    )
                }
            }
        }
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState
        ) { tab ->
            when (tabs[tab]) {
                TabToday.Todo -> TabTodo(
                    state = state,
                    onEvent = onEvent
                )
                TabToday.Done -> TabDone(
                    state = state
                )
            }
        }
    }
}

@Composable
private fun FloatingActionButton(
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null
            )
        },
        text = { Text(text = "Add Todo") },
        onClick = { onClick.invoke() }
    )
}

@Composable
private fun TabTodo(
    state: StateToday,
    onEvent: (EventToday) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.todoTodoTodayResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
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
                            CustomComposableElevatedCard(
                                modifier = Modifier.padding(horizontal = 15.dp),
                                icon = Icons.Rounded.CheckBoxOutlineBlank,
                                title = todoToday.todoTitle,
                                onClick = {}
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        CustomTextContent(
                                            text = todoToday.todoImportance,
                                            isSingleLine = true
                                        )
                                        CustomTextContent(
                                            text = convertLongToString(todoToday.todoDate),
                                            isSingleLine = true
                                        )
                                        CustomTextContent(
                                            text = todoToday.todoContent,
                                            isSingleLine = true
                                        )
                                    }
                                    ElevatedCard(
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        onClick = { onEvent(EventToday.BottomSheetMarkAsDoneVisibility(true, todoToday)) }
                                    ) {
                                        CustomTextContent(
                                            modifier = Modifier.padding(15.dp),
                                            text = "Mark as Done",
                                            isSingleLine = true
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                } else {
                    CustomTextHeader(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Nothing to do today"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.Center),
                    text = state.todoTodoTodayResponse.messageFailed
                )
            }
        }
    }
}

@Composable
private fun TabDone(
    state: StateToday
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.doneTodoTodayResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
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
                            CustomComposableElevatedCard(
                                modifier = Modifier.padding(horizontal = 15.dp),
                                icon = Icons.Rounded.CheckBox,
                                title = todoToday.todoTitle,
                                onClick = {}
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    CustomTextContent(
                                        text = todoToday.todoImportance,
                                        isSingleLine = true
                                    )
                                    CustomTextContent(
                                        text = convertLongToString(todoToday.todoDate),
                                        isSingleLine = true
                                    )
                                    CustomTextContent(
                                        text = todoToday.todoContent,
                                        isSingleLine = true
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                } else {
                    CustomTextHeader(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Not any task done today"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.Center),
                    text = state.doneTodoTodayResponse.messageFailed
                )
            }
        }
    }
}