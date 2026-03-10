package com.a.todo.state

import com.a.todo.enumclass.TodoImportance

data class StateAddTodo(
    val radioButtonTodoImportance: TodoImportance? = null,
    val buttonGroupTodoImportance: String = "",
    val textTodoDay: Int = 1,
    val textFieldTodoTitle: String = "",
    val textFieldTodoContent: String = "",
)
