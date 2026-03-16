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
            firebaseAuth.getAuthState().collect { result ->
                _state.update { it.copy(currentUser = result) }
            }
        }
    }

    fun onAction(actionAccount: ActionAccount) {
        when (actionAccount) {
            ActionAccount.ButtonSignOut -> {
                buttonSignOut()
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