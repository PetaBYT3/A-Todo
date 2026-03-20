package com.a.todo.contract

import com.google.firebase.auth.FirebaseUser

sealed interface ActionSettings {
    data object BottomSheetNoAccount: ActionSettings
    data object BottomSheetDeleteAllData: ActionSettings
    data object ButtonDeleteAllData: ActionSettings
    data object AboutAppClick: ActionSettings
    data class ShowSnackBar(val message: String): ActionSettings
}

data class StateSettings(
    val authState: FirebaseUser? = null,
    val bottomSheetNoAccount: Boolean = false,

    val bottomSheetDeleteAllData: Boolean = false,
    val aboutAppClickCount: Int = 0,
    val aboutAppLastClick: Long = 0
)