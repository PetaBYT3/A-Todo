package com.a.todo.event

import com.a.todo.local.EntityTodo

sealed interface EventToday {
    data class BottomSheetMarkAsDoneVisibility(val isVisible: Boolean, val todoToDelete: EntityTodo?): EventToday
    data object ButtonMarkAsDone: EventToday
}