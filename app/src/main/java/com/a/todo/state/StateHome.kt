package com.a.todo.state

data class StateHome(
    val currentUser: String? = null,
    val cardAnonymousWarn: Boolean = true,

    val bottomSheetSignOut: Boolean = false
)
