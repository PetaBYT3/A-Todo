package com.a.todo.contract

import com.google.firebase.auth.FirebaseUser

sealed interface ActionAccount {
    data object BottomSheetSignOut: ActionAccount
    data object ButtonSignOut: ActionAccount
}

data class StateAccount(
    val isLoading: Boolean = true,
    val currentUser: FirebaseUser? = null,
    val bottomSheetSignOut: Boolean = false
)