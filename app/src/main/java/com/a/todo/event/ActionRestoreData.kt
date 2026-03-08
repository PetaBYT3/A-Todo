package com.a.todo.event

sealed interface ActionRestoreData {
    data object ButtonRestoreData: ActionRestoreData
}