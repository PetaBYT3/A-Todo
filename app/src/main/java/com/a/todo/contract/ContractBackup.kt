package com.a.todo.contract

import com.a.todo.page.AutomaticBackup
import com.a.todo.services.ResponseFirestore

sealed interface ActionBackup {
    data class SetAutomaticBackup(val automaticBackup: AutomaticBackup): ActionBackup
    data object BottomSheetAutomaticBackup: ActionBackup
    data object ButtonBackup: ActionBackup
    data object BottomSheetClearDataOnCloud: ActionBackup
    data object ButtonClearDataOnCloud: ActionBackup
}

data class StateBackup(
    val localTodoSize: Int = 0,
    val todoCloudDescription: ResponseFirestore? = null,
    val automaticBackup: AutomaticBackup = AutomaticBackup.Off,
    val bottomSheetAutomaticBackup: Boolean = false,
    val isButtonBackupNowLoading: Boolean = false,
    val bottomSheetClearDataOnCloud: Boolean = false,
    val isButtonClearDataOnCloudLoading: Boolean = false
)