package com.a.todo.state

import com.google.firebase.auth.FirebaseUser

data class StateEmailVerification(
    val authState: FirebaseUser? = null,
    val isButtonSendEmailVerificationLoading: Boolean = false
)
