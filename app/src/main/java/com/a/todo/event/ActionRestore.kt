package com.a.todo.event

sealed interface ActionRestore {
    data object ButtonRestoreData: ActionRestore
}