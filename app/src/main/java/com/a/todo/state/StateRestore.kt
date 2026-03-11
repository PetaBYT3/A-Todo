package com.a.todo.state

import com.a.todo.services.ResponseFirestore

data class StateRestore(
    val todoCloudDescription: ResponseFirestore? = null,
    val isButtonRestoreLoading: Boolean = false
)
