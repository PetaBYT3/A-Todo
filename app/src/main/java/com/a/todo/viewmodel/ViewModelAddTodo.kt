package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.contract.ActionAddTodo
import com.a.todo.contract.StateAddTodo
import com.a.todo.extension.getFutureDateByDaysAsLong
import com.a.todo.local.EntityTodo
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.repository.ResponseDatabase
import com.a.todo.util.SnackBar
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelAddTodo(
    private val repositoryDatabase: RepositoryDatabase,
    private val snackBar: SnackBar
): ViewModel() {
    private val _popBack = Channel<Unit>()
    val popBack = _popBack.receiveAsFlow()

    private val _state = MutableStateFlow(StateAddTodo())
    val state = _state.onStart {

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateAddTodo()
    )

    fun onEvent(actionAddTodo: ActionAddTodo) {
        when (actionAddTodo) {
            is ActionAddTodo.RadioButtonTodoImportance -> {
                _state.update { it.copy(radioButtonTodoImportance = actionAddTodo.todoImportance) }
            }
            is ActionAddTodo.ButtonGroupTodoImportance -> {
                _state.update { it.copy(buttonGroupTodoImportance = actionAddTodo.todoImportance) }
            }
            ActionAddTodo.ButtonIncreaseTodoDay -> {
                _state.update { it.copy(textTodoDay = it.textTodoDay + 1) }
            }
            ActionAddTodo.ButtonDecreaseTodoDay -> {
                if (_state.value.textTodoDay >= 1) {
                    _state.update { it.copy(textTodoDay = it.textTodoDay - 1) }
                }
            }
            is ActionAddTodo.TextFieldTodoTitle -> {
                _state.update { it.copy(textFieldTodoTitle = actionAddTodo.todoTitle) }
            }
            is ActionAddTodo.TextFieldTodoContent -> {
                _state.update { it.copy(textFieldTodoContent = actionAddTodo.todoContent) }
            }
            is ActionAddTodo.ButtonSaveTodo -> {
                buttonSaveTodo()
            }
        }
    }

    private fun buttonSaveTodo() {
        viewModelScope.launch {
            val entityTodo = EntityTodo(
                todoImportance = _state.value.radioButtonTodoImportance?.value ?: "",
                todoDate = getFutureDateByDaysAsLong(_state.value.textTodoDay),
                todoTitle = _state.value.textFieldTodoTitle,
                todoContent = _state.value.textFieldTodoContent,
                todoStatus = "Todo"
            )
            repositoryDatabase.upsertTodo(entityTodo).collect { result ->
                when (result) {
                    is ResponseDatabase.Success -> {
                        snackBar.showSnackBar(result.messageSuccess)
                        _popBack.send(Unit)
                        delay(500)
                        resetState()
                    }
                    is ResponseDatabase.Failed -> {
                        snackBar.showSnackBar(result.messageFailed)
                    }
                }
            }
        }
    }

    private fun resetState() {
        _state.update { StateAddTodo() }
    }
}