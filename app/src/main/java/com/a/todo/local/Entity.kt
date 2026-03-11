package com.a.todo.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoTable")
data class EntityTodo(
    @PrimaryKey(autoGenerate = true)
    val todoId: Long = 0,
    val todoImportance: String = "",
    val todoDate: Long = 0,
    val todoTitle: String = "",
    val todoContent: String = "",
    val todoStatus: String = ""
)