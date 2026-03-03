package com.a.todo.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.a.todo.R
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.repository.ResponseDatabase
import kotlinx.coroutines.flow.first

class WorkerNotification(
    context: Context,
    params: WorkerParameters,
    private val repositoryDatabase: RepositoryDatabase
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return when (val todoToday = repositoryDatabase.getTodoTodoToday().first()) {
            is ResponseDatabase.Success -> {
                showNotificationTodoToday(todoToday.listTodo.size)
                Result.success()
            }
            is ResponseDatabase.Failed -> Result.failure()
        }
    }

    private fun showNotificationTodoToday(todoCount: Int) {
        val channelId = "DailyTodoReminderV2"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "dailyTodoReminder",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Some Task Waiting For You")
            .setContentText("You have $todoCount task todo today")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(false)
            .build()
        notificationManager.notify(1, notification)
    }
}