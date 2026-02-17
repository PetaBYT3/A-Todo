@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Email
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
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomPasswordTextField
import com.a.todo.design.CustomTextField
import com.a.todo.event.EventSignIn
import com.a.todo.event.EventSignUp
import com.a.todo.state.StateSignUp
import com.a.todo.viewmodel.ViewModelSignUp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageSignUp(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelSignUp = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    Scaffold(
        modifier = Modifier.imePadding(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopBar(
                backStack = backStack
            )
        },
        content = { innerPadding ->
            Content(
                innerPadding = innerPadding,
                state = state,
                onEvent = onEvent
            )
        }
    )
}

@Composable
private fun TopBar(
    backStack: NavBackStack<NavKey>
) {
    LargeTopAppBar(
        navigationIcon = {
            CustomIconButton(
                icon = Icons.Rounded.ArrowBack,
                onClick = { backStack.removeAt(backStack.lastIndex) }
            )
        },
        title = { Text(text = "Sign Up") }
    )
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    state: StateSignUp,
    onEvent: (EventSignUp) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 15.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.textFieldEmail,
            onValueChange = { onEvent(EventSignUp.TextFieldEmail(it)) },
            leadingIcon = Icons.Rounded.Email,
            placeholder = "Email"
        )
        CustomPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.textFieldPassword,
            onValueChange = { onEvent(EventSignUp.TextFieldPassword(it)) },
            placeholder = "Password"
        )
        CustomPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.textFieldRetypePassword,
            onValueChange = { onEvent(EventSignUp.TextFieldRetypePassword(it)) },
            placeholder = "Retype Password"
        )
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Sign Up",
            onClick = { onEvent(EventSignUp.ButtonSignUp) },
            isLoading = state.isButtonSignUpLoading
        )
    }
}