package com.a.todo.state

import com.a.todo.local.EntityTodo
import com.a.todo.repository.ResponseDatabase

data class StateToday(
    val todoTodoTodayResponse: ResponseDatabase? = null,
    val doneTodoTodayResponse: ResponseDatabase? = null,

    val bottomSheetMarkAsDone: Boolean = false,
    val todoToDelete: EntityTodo? = null
)
