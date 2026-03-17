package com.a.todo.contract

import com.google.firebase.auth.FirebaseUser

sealed interface ActionEmailVerification {
    data object ButtonSendEmailVerification: ActionEmailVerification
}

data class StateEmailVerification(
    val authState: FirebaseUser? = null,
    val isButtonSendEmailVerificationLoading: Boolean = false
)
