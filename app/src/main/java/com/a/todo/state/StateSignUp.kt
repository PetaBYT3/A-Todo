package com.a.todo.state

data class StateSignUp(
    val textFieldEmail: String = "",
    val textFieldPassword: String = "",
    val textFieldRetypePassword: String = "",
    val isButtonSignUpLoading: Boolean = false
)
