package com.a.todo.contract

sealed interface ActionSettings {
    data object BottomSheetDeleteAllData: ActionSettings
    data object ButtonDeleteAllData: ActionSettings
    data object AboutAppClick: ActionSettings
}

data class StateSettings(
    val bottomSheetDeleteAllData: Boolean = false,
    val aboutAppClickCount: Int = 0,
    val aboutAppLastClick: Long = 0
)