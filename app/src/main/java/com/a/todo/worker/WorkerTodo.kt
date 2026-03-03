package com.a.todo.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.repository.ResponseDatabase
import kotlinx.coroutines.flow.first

class WorkerTodo(
    context: Context,
    params: WorkerParameters,
    private val repositoryDatabase: RepositoryDatabase
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val result = repositoryDatabase.setUnfinishedTodoToExpired().first()

        return when (result) {
            is ResponseDatabase.Success -> Result.success()
            is ResponseDatabase.Failed -> Result.failure()
        }
    }
}