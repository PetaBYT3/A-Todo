package com.a.todo.contract

sealed interface ActionSignIn {
    data class TextFieldEmail(val email: String): ActionSignIn
    data class TextFieldPassword(val password: String): ActionSignIn
    data object ButtonSignIn: ActionSignIn
}

data class StateSignIn(
    val textFieldEmail: String = "",
    val textFieldPassword: String = "",
    val isButtonSignInLoading: Boolean = false
)
