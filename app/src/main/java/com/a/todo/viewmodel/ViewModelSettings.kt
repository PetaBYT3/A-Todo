package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.ActionSettings
import com.a.todo.services.FirebaseAuth
import com.a.todo.services.FirebaseFirestore
import com.a.todo.services.ResponseFirestore
import com.a.todo.state.StateSettings
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelSettings(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateSettings())
    val state = _state.onStart {
        getAuthState()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateSettings()
    )

    private fun getAuthState() {
        viewModelScope.launch {
            firebaseAuth.getAuthState().collect { result ->
                _state.update { it.copy(currentUser = result) }
            }
        }
    }

    fun onAction(actionSettings: ActionSettings) {
        when (actionSettings) {
            ActionSettings.TestBackupData -> {
                viewModelScope.launch {
                    firebaseFirestore.backupLocalToFirestore().collect {
                        when (it) {
                            is ResponseFirestore.Success -> snackBar.showSnackBar(it.messageSuccess)
                            is ResponseFirestore.Failed -> snackBar.showSnackBar(it.messageFailed)
                        }
                    }
                }
            }
        }
    }
}