package com.a.todo.repository

import com.a.todo.extension.capitalizeEachWord
import com.a.todo.local.Dao
import com.a.todo.local.EntityTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class ResponseDatabase {
    data class Success(
        val messageSuccess: String,
        val listTodo: List<EntityTodo> = emptyList()
    ): ResponseDatabase()
    data class Failed(val messageFailed: String, ): ResponseDatabase()
}

class RepositoryDatabase(
    private val dao: Dao
) {
    fun upsertTodo(todo: EntityTodo): Flow<ResponseDatabase> = flow {
        try {
            when {
                todo.todoImportance.isBlank() -> {
                    emit(ResponseDatabase.Failed("Please Select The Importance"))
                    return@flow
                }
                todo.todoTitle.isBlank() -> {
                    emit(ResponseDatabase.Failed("Title Cannot Be Empty"))
                    return@flow
                }
            }

            dao.upsertTodo(todo)
            emit(ResponseDatabase.Success("Todo Saved"))
        } catch (e: Exception) {
            emit(ResponseDatabase.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun markAsDoneTodo(todo: EntityTodo): Flow<ResponseDatabase> = flow {
        try {
            val updatedTodo = todo.copy(
                todoStatus = "Done"
            )

            dao.upsertTodo(updatedTodo)
            emit(ResponseDatabase.Success("Todo ${updatedTodo.todoTitle} marked as done"))
        } catch (e: Exception) {
            emit(ResponseDatabase.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun getTodoTodoToday(): Flow<ResponseDatabase> = callbackFlow {
        try {
            val formatter = SimpleDateFormat("yyyy MM dd", Locale.getDefault())
            val today = formatter.format(Date())

            dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    val todoDate = formatter.format(todo.todoDate)
                    todoDate == today && todo.todoStatus == "Todo"
                }
            }.collect { result ->
                trySend(ResponseDatabase.Success(
                    messageSuccess = "Success",
                    listTodo = result
                ))
            }
        } catch (e: Exception) {
            trySend(ResponseDatabase.Failed(e.message.toString().capitalizeEachWord()))
            close()
        }
    }.flowOn(Dispatchers.IO)

    fun getDoneTodoToday(): Flow<ResponseDatabase> = callbackFlow {
        try {
            val formatter = SimpleDateFormat("yyyy MM dd", Locale.getDefault())
            val today = formatter.format(Date())

            dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    val todoDate = formatter.format(todo.todoDate)
                    todoDate == today && todo.todoStatus == "Done"
                }
            }.collect { result ->
                trySend(ResponseDatabase.Success(
                    messageSuccess = "Success",
                    listTodo = result
                ))
            }
        } catch (e: Exception) {
            trySend(ResponseDatabase.Failed(e.message.toString().capitalizeEachWord()))
            close()
        }
    }.flowOn(Dispatchers.IO)
}