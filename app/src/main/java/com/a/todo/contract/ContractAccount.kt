package com.a.todo.contract

import com.google.firebase.auth.FirebaseUser

sealed interface ActionAccount {
    data object GetEmailVerification: ActionAccount
    data object BottomSheetSignOut: ActionAccount
    data object ButtonSignOut: ActionAccount
}

data class StateAccount(
    val isLoading: Boolean = true,
    val authState: FirebaseUser? = null,
    val bottomSheetSignOut: Boolean = false
)