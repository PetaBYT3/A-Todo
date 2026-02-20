package com.a.todo.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a.todo.enumclass.TodoImportance
import com.a.todo.enumclass.TodoStatus
import java.util.Date

@Entity(tableName = "")
data class EntityTodo(
    @PrimaryKey(autoGenerate = true)
    val todoId: Long,
    val todoImportance: TodoImportance,
    val todoTitle: String,
    val todoDate: Date,
    val todoContent: String,
    val todoStatus: TodoStatus
)