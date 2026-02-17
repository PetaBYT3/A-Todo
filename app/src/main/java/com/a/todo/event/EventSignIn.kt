package com.a.todo.event

sealed interface EventSignIn {
    data class TextFieldEmail(val email: String): EventSignIn
    data class TextFieldPassword(val password: String): EventSignIn
    data object ButtonSignIn: EventSignIn
    data object ButtonSignInAnonymously: EventSignIn
}