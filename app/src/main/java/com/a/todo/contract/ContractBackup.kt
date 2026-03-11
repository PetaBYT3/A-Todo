package com.a.todo.contract

import com.a.todo.page.AutomaticBackup

sealed interface ActionBackup {
    data class SetAutomaticBackup(val automaticBackup: AutomaticBackup): ActionBackup
    data object BottomSheetAutomaticBackup: ActionBackup
    data object ButtonBackup: ActionBackup
}

data class StateBackup(
    val localTodoSize: Int = 0,
    val automaticBackup: AutomaticBackup = AutomaticBackup.Off,
    val bottomSheetAutomaticBackup: Boolean = false
)