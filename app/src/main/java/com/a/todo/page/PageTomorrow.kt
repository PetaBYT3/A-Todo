@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.DrawerState
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
import androidx.compose.material3.TabRowDefaults
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
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextHeader
import com.a.todo.design.CustomTextTitle
import com.a.todo.design.innerWindowInsets
import com.a.todo.extension.convertLongToString
import com.a.todo.navigation.RoutePage
import com.a.todo.repository.ResponseDatabase
import com.a.todo.state.StateTomorrow
import com.a.todo.viewmodel.ViewModelTomorrow
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageTomorrow(
    backStack: NavBackStack<NavKey>,
    drawerState: DrawerState,
    viewModel: ViewModelTomorrow = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                state = state
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { backStack.add(RoutePage.PageAddTodo) }
            )
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
        title = { Text(text = "Tomorrow") },
        scrollBehavior = scrollBehavior
    )
}

private enum class TabTomorrow(
    val icon: ImageVector,
    val title: String
) {
    Tomorrow(Icons.Rounded.Schedule, "Tomorrow")
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateTomorrow
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding)
    ) {
        val tabs = TabTomorrow.entries
        val pagerState = rememberPagerState(
            pageCount = { tabs.size }
        )
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pagerState.currentPage)
                )
            }
        ) {
            tabs.forEach { tab ->
                Tab(
                    icon = { Icon(tab.icon, null) },
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
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState
        ) { tab ->
            when (tabs[tab]) {
                TabTomorrow.Tomorrow -> TabTomorrow(
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
private fun TabTomorrow(
    state: StateTomorrow
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state.todoTomorrowResponse) {
            null -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
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
                            Column(
                                modifier = Modifier.clickable(enabled = true, onClick = {})
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
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
                                        CustomTextTitle(text = todoToday.todoTitle)
                                        CustomTextContent(text = convertLongToString(todoToday.todoDate))
                                        CustomTextContent(
                                            text = todoToday.todoContent,
                                            isSingleLine = true
                                        )
                                    }
                                    CustomIconButton(
                                        icon = Icons.Rounded.Timer,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }
                } else {
                    CustomTextHeader(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Nothing to do tomorrow"
                    )
                }
            }
            is ResponseDatabase.Failed -> {
                CustomTextHeader(
                    modifier = Modifier.align(Alignment.Center),
                    text = state.todoTomorrowResponse.messageFailed
                )
            }
        }
    }
}