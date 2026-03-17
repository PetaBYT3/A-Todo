@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.a.todo.page

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
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Warning
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
import com.a.todo.contract.ActionAccount
import com.a.todo.contract.StateAccount
import com.a.todo.design.CustomButton
import com.a.todo.design.CustomComposableElevatedCard
import com.a.todo.design.CustomConfirmationBottomSheet
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomOutlinedButton
import com.a.todo.design.CustomTextContent
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

    CustomConfirmationBottomSheet(
        isBottomSheetVisible = state.bottomSheetSignOut,
        title = "Sign Out",
        content = {
            CustomComposableElevatedCard(
                icon = Icons.Rounded.Logout,
                title = "Sign Out ?",
                onClick = {}
            ) {
                CustomTextContent(
                    text = "Are you sure you want to sign out ?"
                )
            }
        },
        onCancel = { onAction(ActionAccount.BottomSheetSignOut) },
        onConfirm = { onAction(ActionAccount.ButtonSignOut) }
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
    Box(
        modifier = Modifier.fillMaxSize().padding(innerPadding)
    ) {
        when {
            state.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state.currentUser == null -> {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    CustomComposableElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                        icon = Icons.Rounded.Warning,
                        title = "No Account",
                        content = {
                            CustomTextContent(
                                text = "Sign in or sign up to create account and have access to cloud"
                            )
                        },
                        onClick = {}
                    )
                    CustomButton(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                        text = "Sign In",
                        onClick = {}
                    )
                    CustomOutlinedButton(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                        text = "Sign Up",
                        onClick = {}
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    CustomComposableElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                        icon = Icons.Rounded.Email,
                        title = "Email",
                        content = {
                            CustomTextContent(
                                text = state.currentUser.email ?: ""
                            )
                        },
                        onClick = {}
                    )
                    CustomComposableElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                        icon = Icons.Rounded.Password,
                        title = "Change Password",
                        content = {
                            CustomTextContent(
                                text = "We will send you a link to change your password from your email"
                            )
                        },
                        onClick = {}
                    )
                    CustomOutlinedButton(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                        text = "Sign Out",
                        onClick = { onAction(ActionAccount.BottomSheetSignOut) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Content(
        innerPadding = PaddingValues(0.dp),
        state = StateAccount(),
        onAction = {}
    )
}