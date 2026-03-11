package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.navigation.RoutePage
import com.a.todo.services.FirebaseAuth
import com.a.todo.services.ResponseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelMain(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {
    private val _isInitialized = MutableStateFlow(true)
    val isInitialized = _isInitialized.asStateFlow()

    private val _navigate = Channel<RoutePage>()
    val navigate = _navigate.receiveAsFlow().onStart {
        initializeAccount()
    }.shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000)
    )

    private fun initializeAccount() {
        viewModelScope.launch {
            firebaseAuth.getAuthState().collect { authState ->
                when {
                    authState != null && !authState.isAnonymous -> {
                        firebaseAuth.checkEmailVerification().collect { emailVerification ->
                            when (emailVerification) {
                                is ResponseAuth.Success -> _navigate.send(RoutePage.PageHome)
                                is ResponseAuth.Failed -> _navigate.send(RoutePage.PageEmailVerification)
                            }
                        }
                    }
                    authState != null && authState.isAnonymous -> {
                        _navigate.send(RoutePage.PageHome)
                    }
                    else -> {
                        _navigate.send(RoutePage.PageSignIn)
                    }
                }
                delay(1_000)
                _isInitialized.update { false }
            }
        }
    }
}