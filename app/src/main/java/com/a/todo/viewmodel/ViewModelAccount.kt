package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.contract.ActionAccount
import com.a.todo.contract.StateAccount
import com.a.todo.services.FirebaseAuth
import com.a.todo.services.ResponseAuth
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelAccount(
    private val firebaseAuth: FirebaseAuth,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateAccount())
    val state = _state.onStart {
        getAuthState()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateAccount()
    )

    private fun getAuthState() {
        viewModelScope.launch {
            firebaseAuth.getAuthState().onStart {
                _state.update { it.copy(isLoading = true) }
            }.collect { result ->
                _state.update { it.copy(authState = result, isLoading = false) }
            }
        }
    }

    fun onAction(actionAccount: ActionAccount) {
        when (actionAccount) {
            ActionAccount.GetEmailVerification -> {
                getEmailVerification()
            }
            ActionAccount.BottomSheetSignOut -> {
                _state.update { it.copy(bottomSheetSignOut = !it.bottomSheetSignOut) }
            }
            ActionAccount.ButtonSignOut -> {
                buttonSignOut()
            }
        }
    }

    private fun getEmailVerification() {
        viewModelScope.launch {
            _state.value.authState?.let { authState ->
                if (authState.isEmailVerified) {
                    snackBar.showSnackBar("Email is already verified")
                } else {
                    firebaseAuth.getEmailVerification().collect { result ->
                        when (result) {
                            is ResponseAuth.Success -> snackBar.showSnackBar(result.messageSuccess)
                            is ResponseAuth.Failed -> snackBar.showSnackBar(result.messageFailed)
                        }
                    }
                }
            }
        }
    }

    private fun buttonSignOut() {
        viewModelScope.launch {
            firebaseAuth.signOut().collect {
                when (it) {
                    is ResponseAuth.Success -> snackBar.showSnackBar(it.messageSuccess)
                    is ResponseAuth.Failed -> snackBar.showSnackBar(it.messageFailed)
                }
            }
        }
    }
}