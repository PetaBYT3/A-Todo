package com.a.todo.services

import com.a.todo.extension.capitalizeEachWord
import com.a.todo.local.Dao
import com.a.todo.local.EntityTodo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

sealed class ResponseFirestore {
    data class Success(val messageSuccess: String): ResponseFirestore()
    data class Failed(val messageFailed: String): ResponseFirestore()
}

class FirebaseFirestore(
    private val firebaseAuth: FirebaseAuth,
    private val dao: Dao
) {
    private val firestore = FirebaseFirestore.getInstance()

    private val currentUser = firebaseAuth.getAuthState()
    private val todoCollection = "todoCollection"
    private val todoList = "todoList"

    fun syncLocalToFirestore(): Flow<ResponseFirestore> = flow {
        try {
            val localTodo = dao.getAllTodo().first()
            val firestoreBatch = firestore.batch()

            localTodo.forEach { todo ->
                val todoRef = firestore
                    .collection(todoCollection)
                    .document(currentUser.first().uid)
                    .collection(todoList)
                    .document(todo.todoId.toString())

                firestoreBatch.set(todoRef, todo)
            }

            firestoreBatch.commit().await()
            emit(ResponseFirestore.Success("Sync To Cloud Success"))
        } catch (e: Exception) {
            emit(ResponseFirestore.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun syncFirestoreToLocal(): Flow<ResponseFirestore> = flow {
        try {
            val snapshotTodo = firestore
                .collection(todoCollection)
                .document(currentUser.first().uid)
                .collection(todoList)
                .get()
                .await()

            val todoData = snapshotTodo.toObjects(EntityTodo::class.java)

            if (todoData.isNotEmpty()) {
                dao.upsertListTodo(todoData)
                emit(ResponseFirestore.Success("Sync To Local Success"))
            } else {
                emit(ResponseFirestore.Success("Todo Not Found In Cloud"))
            }
        } catch (e: Exception) {
            emit(ResponseFirestore.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)
}