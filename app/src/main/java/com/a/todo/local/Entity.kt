package com.a.todo.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a.todo.enumclass.TodoImportance
import com.a.todo.enumclass.TodoStatus
import java.util.Date

@Entity(tableName = "todoTable")
data class EntityTodo(
    @PrimaryKey(autoGenerate = true)
    val todoId: Long = 0,
    val todoImportance: String,
    val todoDate: Date,
    val todoTitle: String,
    val todoContent: String,
    val todoStatus: String
)