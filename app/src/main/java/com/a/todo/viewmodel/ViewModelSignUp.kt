package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.contract.ActionSignUp
import com.a.todo.contract.StateSignUp
import com.a.todo.services.FirebaseAuth
import com.a.todo.services.ResponseAuth
import com.a.todo.util.SnackBar
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelSignUp(
    private val firebaseAuth: FirebaseAuth,
    private val snackBar: SnackBar
): ViewModel() {
    private val _signUpStatus = Channel<Unit>()
    val signUpStatus = _signUpStatus.receiveAsFlow()

    private val _state = MutableStateFlow(StateSignUp())
    val state = _state.onStart {

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateSignUp()
    )

    fun onEvent(actionSignUp: ActionSignUp) {
        when (actionSignUp) {
            is ActionSignUp.TextFieldEmail -> {
                _state.update { it.copy(textFieldEmail = actionSignUp.email) }
            }
            is ActionSignUp.TextFieldPassword -> {
                _state.update { it.copy(textFieldPassword = actionSignUp.password) }
            }
            is ActionSignUp.TextFieldRetypePassword -> {
                _state.update { it.copy(textFieldRetypePassword = actionSignUp.retypePassword) }
            }
            ActionSignUp.ButtonSignUp -> {
                buttonSignUp()
            }
        }
    }

    private fun buttonSignUp() {
        viewModelScope.launch {
            firebaseAuth.signUp(
                email = _state.value.textFieldEmail,
                password = _state.value.textFieldPassword,
                retypePassword = _state.value.textFieldRetypePassword
            ).onStart {
                _state.update { it.copy(isButtonSignUpLoading = true) }
            }.onCompletion {
                _state.update { it.copy(isButtonSignUpLoading = false) }
            }.collect { result ->
                when (result) {
                    is ResponseAuth.Success -> {
                        snackBar.showSnackBar(result.messageSuccess)
                        _signUpStatus.send(Unit)
                    }
                    is ResponseAuth.Failed -> {
                        snackBar.showSnackBar(result.messageFailed)
                    }
                }
            }
        }
    }
}