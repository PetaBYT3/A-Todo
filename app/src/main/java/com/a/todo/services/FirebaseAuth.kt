package com.a.todo.services

import com.a.todo.extension.capitalizeEachWord
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class ResponseAuth {
    data class Success(val messageSuccess: String): ResponseAuth()
    data class Failed(val messageFailed: String): ResponseAuth()
}

class FirebaseAuth {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getAuthState(): Flow<FirebaseUser> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val currentUser = auth.currentUser

            if (currentUser != null) {
                trySend(currentUser)
            }
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

    fun bindAnonymousToEmail(
        email: String,
        password: String,
        retypePassword: String
    ): Flow <ResponseAuth> = flow {
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

            val anonymousCredential = EmailAuthProvider.getCredential(email, password)
            firebaseAuth.currentUser?.linkWithCredential(anonymousCredential)?.await()
            emit(ResponseAuth.Success("Bind Success"))
        } catch (e: Exception) {
            emit(ResponseAuth.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun sendEmailVerification(): Flow<ResponseAuth> = flow {
        try {
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            emit(ResponseAuth.Success("Link Verification Has Been Sent To Your Email"))
        } catch (e: Exception) {
            emit(ResponseAuth.Failed(e.message.toString().capitalizeEachWord()))
        }
    }.flowOn(Dispatchers.IO)

    fun checkEmailVerification(): Flow<ResponseAuth> = callbackFlow {
        try {
            val currentUser = firebaseAuth.currentUser

            if (currentUser == null) {
                trySend(ResponseAuth.Failed("Invalid Session"))
                close()
                return@callbackFlow
            }

            val verification = launch {
                while (isActive) {
                    currentUser.reload().await()
                    val isVerified = currentUser.isEmailVerified

                    if (isVerified) {
                        trySend(ResponseAuth.Success("Email Has Been Verified"))
                        close()
                    } else {
                        trySend(ResponseAuth.Failed("Not Verified"))
                    }
                    delay(3_000)
                }
            }

            awaitClose { verification.cancel() }
        } catch (e: Exception) {
            trySend(ResponseAuth.Failed(e.message.toString().capitalizeEachWord()))
            close()
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