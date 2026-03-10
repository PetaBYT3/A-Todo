@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Restore
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.design.CustomButton
import com.a.todo.design.CustomComposableElevatedCard
import com.a.todo.design.CustomOutlinedButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.innerWindowInsets
import com.a.todo.event.ActionRestoreData
import com.a.todo.state.StateRestoreData
import com.a.todo.viewmodel.ViewModelRestoreData
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageRestoreData(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelRestoreData = koinViewModel()
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
        title = { Text(text = "Restore") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateRestoreData,
    onAction: (ActionRestoreData) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CustomComposableElevatedCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            icon = Icons.Rounded.Restore,
            title = "Restore Data From Cloud",
            onClick = {}
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CustomTextContent(
                    text = "Total Todo :",
                    isSingleLine = true
                )
                CustomTextContent(
                    text = "Last Sync : ",
                    isSingleLine = true
                )
            }
        }
        CustomButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            text = "Restore",
            onClick = {}
        )
        CustomOutlinedButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            text = "Skip",
            onClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    Content(
        innerPadding = PaddingValues(0.dp),
        state = StateRestoreData(),
        onAction = {}
    )
}