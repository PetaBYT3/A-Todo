package com.a.todo.event

sealed interface ActionSettings {
    data object TestBackupData: ActionSettings
}