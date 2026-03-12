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

    @Upsert
    suspend fun upsertListTodo(listTodo: List<EntityTodo>)

    @Delete
    suspend fun delete(todo: EntityTodo)

    @Query("DELETE FROM todoTable")
    suspend fun deleteAllTodo()

    @Query("SELECT * FROM todoTable ORDER BY todoId DESC")
    fun getAllTodo(): Flow<List<EntityTodo>>
}