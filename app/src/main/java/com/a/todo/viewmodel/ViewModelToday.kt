package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.EventToday
import com.a.todo.local.EntityTodo
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.repository.ResponseDatabase
import com.a.todo.state.StateToday
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelToday(
    private val repositoryDatabase: RepositoryDatabase,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateToday())
    val state = _state.onStart {
        getTodoTodoToday()
        getDoneTodoToday()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateToday()
    )

    private fun getTodoTodoToday() {
        viewModelScope.launch {
            repositoryDatabase.getTodoTodoToday().collect { result ->
                _state.update { it.copy(todoTodoTodayResponse = result) }
            }
        }
    }

    private fun getDoneTodoToday() {
        viewModelScope.launch {
            repositoryDatabase.getDoneTodoToday().collect { result ->
                _state.update { it.copy(doneTodoTodayResponse = result) }
            }
        }
    }

    fun onEvent(eventToday: EventToday) {
        when (eventToday) {
            is EventToday.ButtonMarkAsDone -> {
                buttonMarkAsDone(eventToday.markAsDoneTodo)
            }
        }
    }

    private fun buttonMarkAsDone(todo: EntityTodo) {
        viewModelScope.launch {
            repositoryDatabase.markAsDoneTodo(todo).collect { result ->
                when (result) {
                    is ResponseDatabase.Success -> snackBar.showSnackBar(result.messageSuccess)
                    is ResponseDatabase.Failed -> snackBar.showSnackBar(result.messageFailed)
                }
            }
        }
    }
}