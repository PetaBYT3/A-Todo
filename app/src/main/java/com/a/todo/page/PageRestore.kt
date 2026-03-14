@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.a.todo.page

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Restore
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.contract.ActionRestore
import com.a.todo.design.CustomButton
import com.a.todo.design.CustomComposableElevatedCard
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.innerWindowInsets
import com.a.todo.extension.convertDateToStringDate
import com.a.todo.extension.convertDateToStringTime
import com.a.todo.services.ResponseFirestore
import com.a.todo.state.StateRestore
import com.a.todo.viewmodel.ViewModelRestore
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageRestore(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelRestore = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .imePadding()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        contentWindowInsets = innerWindowInsets(),
        topBar = {
            TopBar(
                scrollBehavior = scrollBehaviour,
                onClick = { backStack.removeAt(backStack.lastIndex) }
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
    onClick: () -> Unit
) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            CustomIconButton(
                icon = Icons.Rounded.ArrowBack,
                onClick = { onClick.invoke() }
            )
        },
        title = { Text(text = "Restore") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateRestore,
    onAction: (ActionRestore) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CustomComposableElevatedCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp).animateContentSize(tween()),
            icon = Icons.Rounded.Restore,
            title = "Restore Data From Cloud",
            onClick = {}
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                when (state.todoCloudDescription) {
                    null -> {
                        LoadingIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is ResponseFirestore.Success -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            val totalTodo = state.todoCloudDescription.todoCloudDescription?.totalTodo ?: "0"
                            CustomTextContent(
                                text = "Total Todo : $totalTodo",
                                isSingleLine = true
                            )
                            val lastSync = state.todoCloudDescription.todoCloudDescription?.lastSync
                            CustomTextContent(
                                text = "Last Backup : ${convertDateToStringDate(lastSync)} at ${convertDateToStringTime(lastSync)}",
                                isSingleLine = true
                            )
                        }
                    }
                    is ResponseFirestore.Failed -> {
                        CustomTextContent(
                            modifier = Modifier.align(Alignment.Center),
                            text = state.todoCloudDescription.messageFailed
                        )
                    }
                }
            }
        }
        CustomButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            text = "Restore",
            onClick = { onAction(ActionRestore.ButtonRestoreData) }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    Content(
        innerPadding = PaddingValues(0.dp),
        state = StateRestore(),
        onAction = {}
    )
}