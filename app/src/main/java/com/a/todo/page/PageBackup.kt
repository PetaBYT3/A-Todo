@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.contract.ActionBackup
import com.a.todo.contract.StateBackup
import com.a.todo.design.CustomButton
import com.a.todo.design.CustomComposableBottomSheet
import com.a.todo.design.CustomComposableElevatedCard
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.innerWindowInsets
import com.a.todo.viewmodel.ViewModelBackup
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageBackup(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelBackup = koinViewModel()
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

    CustomComposableBottomSheet(
        isBottomSheetVisible = state.bottomSheetAutomaticBackup,
        title = "Automatic Backup",
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                AutomaticBackup.entries.forEach { automaticBackup ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = if (state.automaticBackup == automaticBackup) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        onClick = {
                            onAction(ActionBackup.SetAutomaticBackup(automaticBackup))
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomTextContent(
                                text = automaticBackup.value
                            )
                        }
                    }
                }
            }
        },
        onCancel = { onAction(ActionBackup.BottomSheetAutomaticBackup) }
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
        title = { Text(text = "Backup") },
        scrollBehavior = scrollBehavior
    )
}

enum class AutomaticBackup(
    val value: String
) {
    Off("Off"),
    Daily("Daily"),
    Weekly("Weekly"),
    Monthly("Monthly")
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateBackup,
    onAction: (ActionBackup) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CustomComposableElevatedCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            icon = Icons.Rounded.Backup,
            title = "Backup",
            onClick = {}
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CustomTextContent(
                    text = "Local"
                )
                CustomTextContent(
                    text = "Total Todo : ${state.localTodoSize}"
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CustomTextContent(
                    text = "Cloud"
                )
                CustomTextContent(
                    text = "Current Todo Data On Local :"
                )
            }
        }
        CustomComposableElevatedCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            icon = Icons.Rounded.Schedule,
            title = "Automatic Backup",
            onClick = {}
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                onClick = { onAction(ActionBackup.BottomSheetAutomaticBackup) }
            ) {
                Row(
                    modifier = Modifier.padding(15.dp)
                ) {
                    CustomTextContent(
                        modifier = Modifier.weight(1f),
                        text = state.automaticBackup.value,
                        isSingleLine = true
                    )
                    Icon(Icons.Rounded.ArrowForward, null)
                }
            }
        }
        CustomButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            text = "Backup Now",
            onClick = {}
        )
    }
}