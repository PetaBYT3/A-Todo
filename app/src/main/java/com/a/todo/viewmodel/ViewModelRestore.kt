package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.ActionRestore
import com.a.todo.services.FirebaseFirestore
import com.a.todo.services.ResponseFirestore
import com.a.todo.state.StateRestore
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelRestore(
    private val firebaseFirestore: FirebaseFirestore,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateRestore())
    val state = _state.onStart {
        getTodoCloudDescription()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateRestore()
    )

    private fun getTodoCloudDescription() {
        viewModelScope.launch {
            firebaseFirestore.getTodoCloudDescription().collect { result ->
                _state.update { it.copy(todoCloudDescription = result) }
            }
        }
    }

    fun onAction(actionRestore: ActionRestore) {
        when (actionRestore) {
            ActionRestore.ButtonRestoreData -> {
                buttonRestoreData()
            }
        }
    }

    private fun buttonRestoreData() {
        viewModelScope.launch {
            firebaseFirestore.syncFirestoreToLocal().onStart {
                _state.update { it.copy(isButtonRestoreLoading = true) }
            }.onCompletion {
                _state.update { it.copy(isButtonRestoreLoading = false) }
            }.collect { result ->
                when (result) {
                    is ResponseFirestore.Success -> snackBar.showSnackBar(result.messageSuccess)
                    is ResponseFirestore.Failed -> snackBar.showSnackBar(result.messageFailed)
                }
            }
        }
    }
}