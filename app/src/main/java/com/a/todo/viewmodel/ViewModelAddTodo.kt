package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.EventAddTodo
import com.a.todo.state.StateAddTodo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ViewModelAddTodo(

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
            is EventAddTodo.TextFieldTodoTitle -> {
                _state.update { it.copy(textFieldTodoTitle = eventAddTodo.todoTitle) }
            }
            is EventAddTodo.TextFieldTodoContent -> {
                _state.update { it.copy(textFieldTodoContent = eventAddTodo.todoContent) }
            }
        }
    }
}