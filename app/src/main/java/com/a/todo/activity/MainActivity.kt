package com.a.todo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.a.todo.design.rootWindowInsets
import com.a.todo.navigation.RoutePage
import com.a.todo.page.PageAddTodo
import com.a.todo.page.PageEmailVerification
import com.a.todo.page.PageHome
import com.a.todo.page.PageRestore
import com.a.todo.page.PageSettings
import com.a.todo.page.PageSignIn
import com.a.todo.page.PageSignUp
import com.a.todo.ui.theme.ATodoTheme
import com.a.todo.util.SnackBar
import com.a.todo.viewmodel.ViewModelMain
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    private val viewModel: ViewModelMain by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { viewModel.isInitialized.value }
        enableEdgeToEdge()
        setContent {
            ATodoTheme {
                val backStack = rememberNavBackStack(
                    RoutePage.PageSignIn
                )

                val snackBar: SnackBar = koinInject()
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
                    contentWindowInsets = rootWindowInsets(),
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
    val transitionDuration = 300
    NavDisplay(
        modifier = modifier.fillMaxSize(),
        backStack = backStack,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            )
        },
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
                is RoutePage.PageEmailVerification -> {
                    NavEntry(navKey) {
                        PageEmailVerification(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageHome -> {
                    NavEntry(navKey) {
                        PageHome(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageAddTodo -> {
                    NavEntry(navKey) {
                        PageAddTodo(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageSettings -> {
                    NavEntry(navKey) {
                        PageSettings(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageRestore -> {
                    NavEntry(navKey) {
                        PageRestore(
                            backStack = backStack
                        )
                    }
                }
                else -> error("Unknown Navigation Key : $navKey")
            }
        }
    )
}