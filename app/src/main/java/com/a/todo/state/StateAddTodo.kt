package com.a.todo.state

data class StateAddTodo(
    val buttonGroupTodoImportance: String = "",
    val textTodoDay: Int = 1,
    val textFieldTodoTitle: String = "",
    val textFieldTodoContent: String = "",
)
