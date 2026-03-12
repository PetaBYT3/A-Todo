package com.a.todo.services

import com.a.todo.extension.capitalizeEachWord
import com.a.todo.local.Dao
import com.a.todo.local.EntityTodo
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

data class TodoCloudDescription(
    @ServerTimestamp
    val lastSync: Date? = null,
    val totalTodo: Int? = null
)

sealed class ResponseFirestore {
    data class Success(
        val messageSuccess: String,
        val todoCloudDescription: TodoCloudDescription? = TodoCloudDescription()
    ): ResponseFirestore()

    data class Failed(
        val messageFailed: String
    ): ResponseFirestore()
}

class FirebaseFirestore(
    private val firebaseAuth: FirebaseAuth,
    private val dao: Dao
) {
    private val firestore = FirebaseFirestore.getInstance()

    private suspend fun firestoreRootRef(): DocumentReference = withContext(Dispatchers.IO) {
        val currentUser = firebaseAuth.getAuthState().filterNotNull().first()

        val rootRef = firestore
            .collection("todoCollection")
            .document(currentUser.uid)

        return@withContext rootRef
    }

    fun getTodoCloudDescription(): Flow<ResponseFirestore> = callbackFlow {
        val todoCloudDescriptionListener = firestoreRootRef().addSnapshotListener { snapshot, exception ->
            when {
                snapshot != null -> {
                    val todoCloudDescription = snapshot.toObject(TodoCloudDescription::class.java)
                    trySend(
                        ResponseFirestore.Success(
                            messageSuccess = "Success",
                            todoCloudDescription = todoCloudDescription
                        )
                    )
                }
                exception != null -> {
                    trySend(ResponseFirestore.Failed(exception.message.toString().capitalizeEachWord()))
                    close()
                }
            }
        }

        awaitClose { todoCloudDescriptionListener.remove() }
    }.flowOn(Dispatchers.IO)

    fun backupLocalToFirestore(): Flow<ResponseFirestore> = flow {
        try {
            val localTodo = dao.getAllTodo().first()
            val firestoreBatch = firestore.batch()

            val todoCloudDescription = TodoCloudDescription().copy(
                totalTodo = localTodo.size
            )
            firestoreBatch.set(firestoreRootRef(), todoCloudDescription, SetOptions.merge())

            localTodo.forEach { todo ->
                val todoRef = firestoreRootRef().collection("todoList").document(todo.todoId.toString())
                firestoreBatch.set(todoRef, todo)
            }

            firestoreBatch.commit().await()
            emit(ResponseFirestore.Success("Backup To Cloud Success"))
        } catch (e: Exception) {
            emit(ResponseFirestore.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun restoreFirestoreToLocal(): Flow<ResponseFirestore> = flow {
        try {
            val snapshot = firestoreRootRef().collection("todoList").get().await()
            val todoData = snapshot.toObjects(EntityTodo::class.java)

            if (todoData.isNotEmpty()) {
                dao.upsertListTodo(todoData)
                emit(ResponseFirestore.Success("Restore From Cloud Success"))
            } else {
                emit(ResponseFirestore.Success("Todo Not Found In Cloud"))
            }
        } catch (e: Exception) {
            emit(ResponseFirestore.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun clearDataOnFirestore(): Flow<ResponseFirestore> = flow {
        try {
            val firestoreBatch = firestore.batch()

            val todoListCollection = firestoreRootRef().collection("todoList").get().await()

            for (todo in todoListCollection) {
                firestoreBatch.delete(todo.reference)
            }
            firestoreBatch.delete(firestoreRootRef())

            firestoreBatch.commit().await()
            emit(ResponseFirestore.Success("Data Removed"))
        } catch (e: Exception) {
            emit(ResponseFirestore.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)
}