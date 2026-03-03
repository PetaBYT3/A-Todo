package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.ActionEmailVerification
import com.a.todo.services.FirebaseAuth
import com.a.todo.services.ResponseAuth
import com.a.todo.state.StateEmailVerification
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelEmailVerification(
    private val firebaseAuth: FirebaseAuth,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateEmailVerification())
    val state = _state.onStart {
        getAuthState()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateEmailVerification()
    )

    private fun getAuthState() {
        viewModelScope.launch {
            firebaseAuth.getAuthState().collect { result ->
                _state.update { it.copy(authState = result) }
            }
        }
    }

    fun onAction(actionEmailVerification: ActionEmailVerification) {
        when (actionEmailVerification) {
            ActionEmailVerification.ButtonSendEmailVerification -> {
                buttonSendEmailVerification()
            }
        }
    }

    private fun buttonSendEmailVerification() {
        viewModelScope.launch {
            firebaseAuth.sendEmailVerification().onStart {
                _state.update { it.copy(isButtonSendEmailVerificationLoading = true) }
            }.onCompletion {
                _state.update { it.copy(isButtonSendEmailVerificationLoading = false) }
            }.collect { result ->
                when (result) {
                    is ResponseAuth.Success -> snackBar.showSnackBar(result.messageSuccess)
                    is ResponseAuth.Failed -> snackBar.showSnackBar(result.messageFailed)
                }
            }
        }
    }
}