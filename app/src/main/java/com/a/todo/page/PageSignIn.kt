@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.a.todo.design.CustomButton
import com.a.todo.design.CustomOutlinedButton
import com.a.todo.design.CustomPasswordTextField
import com.a.todo.design.CustomTextField
import com.a.todo.event.EventSignIn
import com.a.todo.navigation.RoutePage
import com.a.todo.state.StateSignIn
import com.a.todo.viewmodel.ViewModelSignIn
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageSignIn(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelSignIn = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    Scaffold(
        modifier = Modifier.imePadding(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { TopBar() },
        content = { innerPadding ->
            Content(
                backStack = backStack,
                state = state,
                onEvent = onEvent,
                innerPadding = innerPadding
            )
        }
    )
}

@Composable
private fun TopBar(
) {
    LargeTopAppBar(
        title = { Text(text = "Sign In") }
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    state: StateSignIn,
    onEvent: (EventSignIn) -> Unit,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier.padding(innerPadding).padding(horizontal = 15.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.textFieldEmail,
            onValueChange = { onEvent(EventSignIn.TextFieldEmail(it)) },
            leadingIcon = Icons.Rounded.Email,
            placeholder = "Email"
        )
        CustomPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.textFieldPassword,
            onValueChange = { onEvent(EventSignIn.TextFieldPassword(it)) },
            placeholder = "Password"
        )
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Sign In",
            isLoading = state.isButtonSignInLoading,
            onClick = { onEvent(EventSignIn.ButtonSignIn) }
        )
        CustomOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Sign Up",
            onClick = { backStack.add(RoutePage.PageSignUp) }
        )
        CustomOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Continue Anonymously",
            isLoading = state.isButtonSignInAnonymouslyLoading,
            onClick = { onEvent(EventSignIn.ButtonSignInAnonymously) }
        )
    }
}