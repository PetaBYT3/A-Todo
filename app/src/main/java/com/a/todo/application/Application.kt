package com.a.todo.application

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.a.todo.extension.getDelayUntilMidnight
import com.a.todo.extension.getDelayUntilMorning
import com.a.todo.module.ModuleApp
import com.a.todo.worker.WorkerNotification
import com.a.todo.worker.WorkerTodo
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class Application: Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(ModuleApp.getAllModules())
        }

        setupWorkerTodo(this@Application)
        setupWorkerNotification(this@Application)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(KoinWorkerFactory()).build()

    private fun setupWorkerTodo(context: Context) {
        val delay = getDelayUntilMidnight()

        val workerTodo = PeriodicWorkRequestBuilder<WorkerTodo>(
            24, TimeUnit.HOURS
        ).setInitialDelay(delay, TimeUnit.MINUTES).addTag("workerTodoExpired").build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = "WorkerTodoExpired",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
            request = workerTodo
        )
    }

    private fun setupWorkerNotification(context: Context) {
        val delay = getDelayUntilMorning()

        val workerNotification = PeriodicWorkRequestBuilder<WorkerNotification>(
            24, TimeUnit.HOURS
        ).setInitialDelay(delay, TimeUnit.MINUTES).addTag("workerTodoNotification").build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = "WorkerTodoNotification",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
            request = workerNotification
        )
    }
}