package com.a.todo.contract

import com.a.todo.local.EntityTodo
import com.a.todo.repository.ResponseDatabase

sealed interface ActionToday {
    data class BottomSheetMarkAsDoneVisibility(val isVisible: Boolean, val todoToDelete: EntityTodo?): ActionToday
    data object ButtonMarkAsDone: ActionToday
}

data class StateToday(
    val todoTodoTodayResponse: ResponseDatabase? = null,
    val doneTodoTodayResponse: ResponseDatabase? = null,

    val bottomSheetMarkAsDone: Boolean = false,
    val todoToDelete: EntityTodo? = null
)