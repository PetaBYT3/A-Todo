package com.a.todo.event

sealed interface EventHome {
    data object CardAnonymousWarnButtonDismiss: EventHome
    data object ButtonSignOut: EventHome
}