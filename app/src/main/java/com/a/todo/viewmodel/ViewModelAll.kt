package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.state.StateAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelAll(
    private val repositoryDatabase: RepositoryDatabase
): ViewModel() {
    private val _state = MutableStateFlow(StateAll())
    val state = _state.onStart {
        getTodoAllTodo()
        getTodoAllDone()
        getTodoAllExpired()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateAll()
    )

    private fun getTodoAllTodo() {
        viewModelScope.launch {
            repositoryDatabase.getTodoAllTodo().collect { result ->
                _state.update { it.copy(todoAllTodoResponse = result) }
            }
        }
    }

    private fun getTodoAllDone() {
        viewModelScope.launch {
            repositoryDatabase.getTodoAllDone().collect { result ->
                _state.update { it.copy(todoAllDoneResponse = result) }
            }
        }
    }

    private fun getTodoAllExpired() {
        viewModelScope.launch {
            repositoryDatabase.getTodoAllExpired().collect { result ->
                _state.update { it.copy(todoAllExpiredResponse = result) }
            }
        }
    }
}