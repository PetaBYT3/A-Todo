package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.EventAddTodo
import com.a.todo.extension.getFutureDateByDaysAsDate
import com.a.todo.local.Dao
import com.a.todo.local.EntityTodo
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.repository.ResponseDatabase
import com.a.todo.state.StateAddTodo
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelAddTodo(
    private val repositoryDatabase: RepositoryDatabase,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateAddTodo())
    val state = _state.onStart {

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateAddTodo()
    )

    fun onEvent(eventAddTodo: EventAddTodo) {
        when (eventAddTodo) {
            is EventAddTodo.ButtonGroupTodoImportance -> {
                _state.update { it.copy(buttonGroupTodoImportance = eventAddTodo.todoImportance) }
            }
            EventAddTodo.ButtonIncreaseTodoDay -> {
                _state.update { it.copy(textTodoDay = it.textTodoDay + 1) }
            }
            EventAddTodo.ButtonDecreaseTodoDay -> {
                if (_state.value.textTodoDay >= 1) {
                    _state.update { it.copy(textTodoDay = it.textTodoDay - 1) }
                }
            }
            is EventAddTodo.TextFieldTodoTitle -> {
                _state.update { it.copy(textFieldTodoTitle = eventAddTodo.todoTitle) }
            }
            is EventAddTodo.TextFieldTodoContent -> {
                _state.update { it.copy(textFieldTodoContent = eventAddTodo.todoContent) }
            }
            is EventAddTodo.ButtonSaveTodo -> {
                buttonSaveTodo()
            }
        }
    }

    private fun buttonSaveTodo() {
        viewModelScope.launch {
            val entityTodo = EntityTodo(
                todoImportance = _state.value.buttonGroupTodoImportance,
                todoDate = getFutureDateByDaysAsDate(_state.value.textTodoDay),
                todoTitle = _state.value.textFieldTodoTitle,
                todoContent = _state.value.textFieldTodoContent,
                todoStatus = "Todo"
            )
            repositoryDatabase.upsertTodo(entityTodo).collect { result ->
                when (result) {
                    is ResponseDatabase.Success -> {
                        snackBar.showSnackBar(result.messageSuccess)
                    }
                    is ResponseDatabase.Failed -> {
                        snackBar.showSnackBar(result.messageFailed)
                    }
                }
            }
        }
    }
}