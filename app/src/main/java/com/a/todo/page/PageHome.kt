@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.navigation.RoutePage
import com.a.todo.viewmodel.ViewModelHome
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private enum class DrawerTab(
    val icon: ImageVector,
    val title: String
) {
    Today(Icons.Rounded.CalendarToday, "Today"),
    Tomorrow(Icons.Rounded.Schedule, "Tomorrow"),
    All(Icons.Rounded.Menu, "All"),
    Settings(Icons.Rounded.Settings, "Settings")
}

@Composable
fun PageHome(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelHome = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    var currentTab by rememberSaveable { mutableStateOf(DrawerTab.Today) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
                Column(
                    modifier = Modifier.fillMaxSize().padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    DrawerTab.entries.forEach { tab ->
                        NavigationDrawerItem(
                            icon = { Icon(tab.icon, null) },
                            label = { Text(text = tab.title) },
                            selected = currentTab == tab,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }.invokeOnCompletion {
                                    if (tab == DrawerTab.Settings) {
                                        backStack.add(RoutePage.PageSettings)
                                    } else {
                                        currentTab = tab
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = currentTab,
                label = "localNavTransition",
                transitionSpec = { fadeIn(tween()) togetherWith fadeOut(tween())}
            ) { targetTab ->
                when (targetTab) {
                    DrawerTab.Today -> PageToday(
                        backStack = backStack,
                        drawerState = drawerState
                    )
                    DrawerTab.Tomorrow -> PageTomorrow(
                        backStack = backStack,
                        drawerState = drawerState
                    )
                    DrawerTab.All -> PageAll(
                        backStack = backStack,
                        drawerState = drawerState
                    )
                    else -> {}
                }
            }
        }
    }
}