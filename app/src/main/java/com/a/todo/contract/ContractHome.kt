package com.a.todo.contract

sealed interface ActionHome {
    data object CardAnonymousWarnButtonDismiss: ActionHome
    data object BottomSheetSignOut: ActionHome
    data object ButtonSignOut: ActionHome
}

data class StateHome(
    val currentUser: String? = null,
    val cardAnonymousWarn: Boolean = true,

    val bottomSheetSignOut: Boolean = false
)