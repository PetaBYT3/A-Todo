package com.a.todo.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "")
data class EntityTodo(
    @PrimaryKey(autoGenerate = true)
    val todoId: Long
)