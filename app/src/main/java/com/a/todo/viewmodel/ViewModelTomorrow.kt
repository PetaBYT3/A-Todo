package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.state.StateTomorrow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelTomorrow(
    private val repositoryDatabase: RepositoryDatabase
): ViewModel() {
    private val _state = MutableStateFlow(StateTomorrow())
    val state = _state.onStart {
        getTodoTomorrow()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateTomorrow()
    )

    private fun getTodoTomorrow() {
        viewModelScope.launch {
            repositoryDatabase.getTodoTomorrow().collect { result ->
                _state.update { it.copy(todoTomorrowResponse = result) }
            }
        }
    }
}