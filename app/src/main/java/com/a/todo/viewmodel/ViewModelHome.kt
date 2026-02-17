package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.EventHome
import com.a.todo.services.FirebaseAuth
import com.a.todo.services.ResponseAuth
import com.a.todo.state.StateHome
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewModelHome(
    private val firebaseAuth: FirebaseAuth,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateHome())
    val state = _state.onStart {

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateHome()
    )

    fun onEvent(eventHome: EventHome) {
        when (eventHome) {
            EventHome.ButtonSignOut -> {
                buttonSignOut()
            }
        }
    }

    private fun buttonSignOut() {
        viewModelScope.launch {
            firebaseAuth.signOut().collect { result ->
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