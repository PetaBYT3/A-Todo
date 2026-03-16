@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.contract.ActionAccount
import com.a.todo.contract.StateAccount
import com.a.todo.design.CustomIconButton
import com.a.todo.design.innerWindowInsets
import com.a.todo.viewmodel.ViewModelAccount
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageAccount(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelAccount = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.imePadding().nestedScroll(scrollBehaviour.nestedScrollConnection),
        contentWindowInsets = innerWindowInsets(),
        topBar = {
            TopBar(
                scrollBehavior = scrollBehaviour,
                onNavigationClick = { backStack.removeAt(backStack.lastIndex) }
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
                icon = Icons.Rounded.ArrowBack,
                onClick = { onNavigationClick.invoke() }
            )
        },
        title = { Text(text = "Account") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateAccount,
    onAction: (ActionAccount) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
    }
}