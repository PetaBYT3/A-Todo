package com.a.todo.repository

import com.a.todo.extension.capitalizeEachWord
import com.a.todo.extension.convertLongToString
import com.a.todo.local.Dao
import com.a.todo.local.EntityTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

sealed class ResponseDatabase {
    data class Success(
        val messageSuccess: String,
        val listTodo: List<EntityTodo> = emptyList()
    ): ResponseDatabase()
    data class Failed(val messageFailed: String): ResponseDatabase()
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
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            val today = LocalDate.now().format(formatter)

            dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    val todoDate = convertLongToString(todo.todoDate)
                    todoDate == today && todo.todoStatus == "Todo"
                }.sortedBy { todo ->
                    when (todo.todoImportance) {
                        "High" -> 1
                        "Medium" -> 2
                        "Low" -> 3
                        else -> 4
                    }
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
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            val today = LocalDate.now().format(formatter)

            dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    val todoDate = convertLongToString(todo.todoDate)
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

    fun getTodoTomorrow(): Flow<ResponseDatabase> = callbackFlow {
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            val tomorrow = LocalDate.now().plusDays(1).format(formatter)

            dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    val todoDate = convertLongToString(todo.todoDate)
                    todoDate == tomorrow
                }.sortedBy { todo ->
                    when (todo.todoImportance) {
                        "High" -> 1
                        "Medium" -> 2
                        "Low" -> 3
                        else -> 4
                    }
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

    fun getTodoAllTodo(): Flow<ResponseDatabase> = callbackFlow {
        try {
            dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    todo.todoStatus == "Todo"
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

    fun getTodoAllDone(): Flow<ResponseDatabase> = callbackFlow {
        try {
            dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    todo.todoStatus == "Done"
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

    fun getTodoAllExpired(): Flow<ResponseDatabase> = callbackFlow {
        try {
            dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    todo.todoStatus == "Expired"
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

    fun setUnfinishedTodoToExpired(): Flow<ResponseDatabase> = flow {
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            val yesterday = LocalDate.now().minusDays(1).format(formatter)

            val yesterdayTodo = dao.getAllTodo().map { allTodo ->
                allTodo.filter { todo ->
                    val todoDate = convertLongToString(todo.todoDate)
                    todoDate == yesterday
                }
            }.first()

            if (yesterdayTodo.isNotEmpty()) {
                val expiredTodo = yesterdayTodo.map { it.copy(todoStatus = "Expired") }
                dao.upsertListTodo(expiredTodo)
            }

            emit(ResponseDatabase.Success(messageSuccess = "Expired todo updated"))
        } catch (e: Exception) {
            emit(ResponseDatabase.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)
}