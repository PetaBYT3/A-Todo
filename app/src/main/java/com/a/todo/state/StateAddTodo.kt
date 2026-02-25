package com.a.todo.state

import java.util.Date

data class StateAddTodo(
    val buttonGroupTodoImportance: String = "",
    val todoDate: Date? = null,
    val textFieldTodoTitle: String = "",
    val textFieldTodoContent: String = "",
)
