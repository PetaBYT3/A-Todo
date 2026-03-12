package com.a.todo.contract

import com.a.todo.services.ResponseFirestore

sealed interface ActionRestore {
    data object ButtonRestoreData: ActionRestore
    data object ButtonDeleteDataOnLocal: ActionRestore
}

data class StateRestore(
    val todoCloudDescription: ResponseFirestore? = null,
    val isButtonRestoreLoading: Boolean = false
)