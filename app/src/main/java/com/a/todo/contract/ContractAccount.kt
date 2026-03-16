package com.a.todo.contract

import com.google.firebase.auth.FirebaseUser

sealed interface ActionAccount {
    data object ButtonSignOut: ActionAccount
}

data class StateAccount(
    val currentUser: FirebaseUser? = null
)