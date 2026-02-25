package com.a.todo.state

import com.a.todo.local.EntityTodo

data class StateToday(
    val todoToday: List<EntityTodo> = emptyList()
)
