package com.a.todo.state

data class StateSignIn(
    val textFieldEmail: String = "",
    val textFieldPassword: String = "",
    val isButtonSignInLoading: Boolean = false,
    val isButtonSignInAnonymouslyLoading: Boolean = false
)
