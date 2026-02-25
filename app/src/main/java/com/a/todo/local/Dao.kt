package com.a.todo.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Upsert
    suspend fun upsertTodo(todo: EntityTodo)

    @Delete
    suspend fun delete(todo: EntityTodo)

    @Query("SELECT * FROM todoTable ORDER BY todoId DESC")
    fun getAllTodo(): Flow<List<EntityTodo>>
}