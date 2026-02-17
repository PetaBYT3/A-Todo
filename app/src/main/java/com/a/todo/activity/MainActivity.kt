package com.a.todo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.a.todo.navigation.RoutePage
import com.a.todo.page.PageHome
import com.a.todo.page.PageSignIn
import com.a.todo.page.PageSignUp
import com.a.todo.ui.theme.ATodoTheme
import com.a.todo.util.SnackBar
import com.a.todo.viewmodel.ViewModelMain
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ATodoTheme {
                val backStack = rememberNavBackStack(
                    RoutePage.PageSignIn
                )

                val viewModel = koinViewModel<ViewModelMain>()
                val snackBar = koinInject<SnackBar>()
                val snackBarHostState = remember { SnackbarHostState() }

                LaunchedEffect(viewModel.navigate) {
                    viewModel.navigate.collect { targetPage ->
                        backStack.clear()
                        backStack.add(targetPage)
                    }
                }

                LaunchedEffect(snackBar.snackBarMessage) {
                    snackBar.snackBarMessage.collect { snackBarMessage ->
                        snackBarHostState.showSnackbar(
                            message = snackBarMessage,
                            withDismissAction = true
                        )
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize().imePadding(),
                    content = { innerPadding ->
                        NavDisplayContainer(
                            modifier = Modifier.padding(innerPadding),
                            backStack = backStack
                        )
                    },
                    snackbarHost = { SnackbarHost(snackBarHostState) }
                )
            }
        }
    }
}

@Composable
private fun NavDisplayContainer(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>
) {
    NavDisplay(
        modifier = modifier.imePadding(),
        backStack = backStack,
        entryProvider = { navKey ->
            when (navKey) {
                is RoutePage.PageSignIn -> {
                    NavEntry(navKey) {
                        PageSignIn(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageSignUp -> {
                    NavEntry(navKey) {
                        PageSignUp(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageHome -> {
                    NavEntry(navKey) {
                        PageHome()
                    }
                }
                else -> error("Unknown Navigation Key : $navKey")
            }
        },
        transitionSpec = {
            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
        },
        popTransitionSpec = {
            slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
        }
    )
}