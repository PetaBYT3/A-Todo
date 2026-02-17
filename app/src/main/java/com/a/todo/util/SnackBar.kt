package com.a.todo.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SnackBar {
    private val _snackBarMessage = Channel<String>()
    val snackBarMessage = _snackBarMessage.receiveAsFlow()

    suspend fun showSnackBar(snackBarMessage: String) {
        _snackBarMessage.send(snackBarMessage)
    }
}