@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.todo.design.CustomBoxCard
import com.a.todo.design.CustomButton
import com.a.todo.design.CustomColumnCard
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomRowCard
import com.a.todo.design.CustomSingleButtonGroup
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextTitle
import com.a.todo.design.InnerWindowInsets
import com.a.todo.event.EventHome
import com.a.todo.state.StateHome
import com.a.todo.viewmodel.ViewModelHome
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageHome(
    viewModel: ViewModelHome = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.imePadding().nestedScroll(scrollBehaviour.nestedScrollConnection),
        contentWindowInsets = InnerWindowInsets,
        topBar = {
            TopBar(
                scrollBehavior = scrollBehaviour
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
                onEvent = onEvent
            )
        }
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        title = { Text(text = "Home") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateHome,
    onEvent: (EventHome) -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val buttonList = listOf("Today", "Tomorrow", "All")
        var buttonGroupState by rememberSaveable { mutableStateOf(buttonList[0]) }
        val pagerState = rememberPagerState(pageCount = { buttonList.size })
        CustomSingleButtonGroup(
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { pagerIndex ->
            when (pagerIndex) {
                0 -> PagerToday()
                1 -> PagerTomorrow()
                2 -> PagerAll()
            }
        }
    }
}

@Composable
private fun FloatingActionButton(
    onEvent: (EventHome) -> Unit
) {
    ExtendedFloatingActionButton(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null
            )
        },
        text = { Text(text = "Add Todo") },
        onClick = {}
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    Content(
        innerPadding = PaddingValues(0.dp),
        state = StateHome(),
        onEvent = {}
    )
}