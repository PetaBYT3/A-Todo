package com.a.todo.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.a.todo.services.FirebaseFirestore
import com.a.todo.services.ResponseFirestore
import kotlinx.coroutines.flow.first

class WorkerBackup(
    context: Context,
    params: WorkerParameters,
    private val firebaseFirestore: FirebaseFirestore
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return when (val backupResult = firebaseFirestore.backupLocalToFirestore().first()) {
            is ResponseFirestore.Success -> {
                Result.success()
            }
            is ResponseFirestore.Failed -> Result.failure()
        }
    }
}