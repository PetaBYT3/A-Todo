package com.a.todo.services

import com.a.todo.extension.capitalizeEachWord
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

sealed class ResponseAuth {
    data class Success(val messageSuccess: String): ResponseAuth()
    data class Failed(val messageFailed: String): ResponseAuth()
}

class FirebaseAuth {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }.flowOn(Dispatchers.IO)

    fun signUp(
        email: String,
        password: String,
        retypePassword: String
    ): Flow<ResponseAuth> = flow {
        try {
            when {
                email.isBlank() || password.isBlank() || retypePassword.isBlank() -> {
                    emit(ResponseAuth.Failed("Please Fill All Text Field"))
                    return@flow
                }
                password != retypePassword -> {
                    emit(ResponseAuth.Failed("Password Not Match"))
                    return@flow
                }
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(ResponseAuth.Success("Sign Up Success"))
        } catch (e: Exception) {
            emit(ResponseAuth.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResponseAuth> = flow {
        try {
            when {
                email.isBlank() -> {
                    emit(ResponseAuth.Failed("Email Cannot Be Empty"))
                    return@flow
                }
                password.isBlank() -> {
                    emit(ResponseAuth.Failed("Password Cannot Be Empty"))
                    return@flow
                }
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(ResponseAuth.Success("Sign In Success"))
        } catch (e: Exception) {
            emit(ResponseAuth.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun signInAnonymously(): Flow<ResponseAuth> = flow {
        try {
            firebaseAuth.signInAnonymously().await()
            emit(ResponseAuth.Success("Sign In Anonymously"))
        } catch (e: Exception) {
            emit(ResponseAuth.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun signOut(): Flow<ResponseAuth> = flow {
        try {
            firebaseAuth.signOut()
            emit(ResponseAuth.Success("Sign Out Success"))
        } catch (e: Exception) {
            emit(ResponseAuth.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)
}