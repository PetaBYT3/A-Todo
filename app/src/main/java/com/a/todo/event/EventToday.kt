package com.a.todo.event

import com.a.todo.local.EntityTodo

sealed interface EventToday {
    data class ButtonMarkAsDone(val markAsDoneTodo: EntityTodo): EventToday
}