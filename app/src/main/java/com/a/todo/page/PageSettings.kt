@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.a.todo.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.design.CustomComposableElevatedCard
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.innerWindowInsets
import com.a.todo.event.ActionSettings
import com.a.todo.state.StateSettings
import com.a.todo.viewmodel.ViewModelSettings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageSettings(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelSettings = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.imePadding().nestedScroll(scrollBehaviour.nestedScrollConnection),
        contentWindowInsets = innerWindowInsets(),
        topBar = {
            TopBar(
                backStack = backStack,
                scrollBehavior = scrollBehaviour
            )
        },
        content = { innerPadding ->
            Content(
                innerPadding = innerPadding,
                state = state,
                onAction = onAction
            )
        }
    )
}

@Composable
private fun TopBar(
    backStack: NavBackStack<NavKey>,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
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
        title = { Text(text = "Settings") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateSettings,
    onAction: (ActionSettings) -> Unit
) {
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            CustomComposableElevatedCard(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                icon = Icons.Rounded.Person,
                title = "Account",
                onClick = {}
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when {
                        state.currentUser == null -> {
                            LoadingIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        else -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CustomTextContent(
                                    modifier = Modifier.weight(1f),
                                    text = when {
                                        state.currentUser.isAnonymous -> "Anonymous"
                                        else -> state.currentUser.email ?: "Email"
                                    },
                                    isSingleLine = true
                                )
                                AnimatedVisibility(
                                    visible = state.currentUser.isAnonymous,
                                    enter = fadeIn(tween()),
                                    exit = fadeOut(tween())
                                ) {
                                    ElevatedCard(
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        onClick = {  }
                                    ) {
                                        CustomTextContent(
                                            modifier = Modifier.padding(15.dp),
                                            text = "Bind Account",
                                            isSingleLine = true
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}