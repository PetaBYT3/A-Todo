package com.a.todo.event

sealed interface ActionEmailVerification {
    data object ButtonSendEmailVerification: ActionEmailVerification
}