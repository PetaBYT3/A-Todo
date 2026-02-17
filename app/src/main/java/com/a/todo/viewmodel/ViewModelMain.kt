package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.navigation.RoutePage
import com.a.todo.services.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class ViewModelMain(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {
    private val _navigate = Channel<RoutePage>()
    val navigate = _navigate.receiveAsFlow().onStart {
        initializeAuth()
    }.shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000)
    )

    private fun initializeAuth() {
        viewModelScope.launch {
            firebaseAuth.getAuthState().collect { authState ->
                when {
                    authState != null -> {
                        _navigate.send(RoutePage.PageHome)
                    }
                    else -> {
                        _navigate.send(RoutePage.PageSignIn)
                    }
                }
            }
        }
    }
}