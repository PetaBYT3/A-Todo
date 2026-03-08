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
import androidx.compose.material.icons.rounded.Backup
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.design.CustomButton
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextTitle
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
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Backup,
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    CustomTextTitle(
                        text = "Restore Data From Cloud",
                        isSingleLine = true
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.5.dp)
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
            }
        }
        CustomButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            text = "Restore",
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