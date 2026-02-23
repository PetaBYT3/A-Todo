package com.a.todo.state

import com.a.todo.enumclass.TodoImportance

data class StateAddTodo(
    val buttonGroupTodoImportance: TodoImportance? = null
)
