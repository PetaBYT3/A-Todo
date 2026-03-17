package com.a.todo.contract

sealed interface ActionSignUp {
    data class TextFieldEmail(val email: String): ActionSignUp
    data class TextFieldPassword(val password: String): ActionSignUp
    data class TextFieldRetypePassword(val retypePassword: String): ActionSignUp
    data object ButtonSignUp: ActionSignUp
}

data class StateSignUp(
    val textFieldEmail: String = "",
    val textFieldPassword: String = "",
    val textFieldRetypePassword: String = "",
    val isButtonSignUpLoading: Boolean = false
)