package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.EventSignIn
import com.a.todo.services.FirebaseAuth
import com.a.todo.services.ResponseAuth
import com.a.todo.state.StateSignIn
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelSignIn(
    private val firebaseAuth: FirebaseAuth,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateSignIn())
    val state = _state.onStart {

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateSignIn()
    )

    fun onEvent(eventSignIn: EventSignIn) {
        when (eventSignIn) {
            is EventSignIn.TextFieldEmail -> {
                _state.update { it.copy(textFieldEmail = eventSignIn.email) }
            }
            is EventSignIn.TextFieldPassword -> {
                _state.update { it.copy(textFieldPassword = eventSignIn.password) }
            }
            EventSignIn.ButtonSignIn -> {
                buttonSignIn()
            }
            EventSignIn.ButtonSignInAnonymously -> {
                buttonSignInAnonymously()
            }
        }
    }

    private fun buttonSignIn() {
        viewModelScope.launch {
            firebaseAuth.signInWithEmailAndPassword(
                email = _state.value.textFieldEmail,
                password = _state.value.textFieldPassword
            ).onStart {
                _state.update { it.copy(isButtonSignInLoading = true) }
            }.onCompletion {
                _state.update { it.copy(isButtonSignInLoading = false) }
            }.collect { result ->
                when (result) {
                    is ResponseAuth.Success -> {
                        snackBar.showSnackBar(result.messageSuccess)
                    }
                    is ResponseAuth.Failed -> {
                        snackBar.showSnackBar(result.messageFailed)
                    }
                }
            }
        }
    }

    private fun buttonSignInAnonymously() {
        viewModelScope.launch {
            firebaseAuth.signInAnonymously().onStart {
                _state.update { it.copy(isButtonSignInAnonymouslyLoading = true) }
            }.onCompletion {
                _state.update { it.copy(isButtonSignInAnonymouslyLoading = false) }
            }.collect { result ->
                when (result) {
                    is ResponseAuth.Success -> {
                        snackBar.showSnackBar(result.messageSuccess)
                    }
                    is ResponseAuth.Failed -> {
                        snackBar.showSnackBar(result.messageFailed)
                    }
                }
            }
        }
    }
}