package com.a.todo.state

import com.a.todo.repository.ResponseDatabase

data class StateAll(
    val todoAllTodoResponse: ResponseDatabase? = null,
    val todoAllDoneResponse: ResponseDatabase? = null,
    val todoAllExpiredResponse: ResponseDatabase? = null
)
