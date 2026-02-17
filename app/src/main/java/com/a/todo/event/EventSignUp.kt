package com.a.todo.event

sealed interface EventSignUp {
    data class TextFieldEmail(val email: String): EventSignUp
    data class TextFieldPassword(val password: String): EventSignUp
    data class TextFieldRetypePassword(val retypePassword: String): EventSignUp
    data object ButtonSignUp: EventSignUp
}