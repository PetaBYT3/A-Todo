package com.a.todo.application

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.a.todo.extension.getDelayUntilMidnight
import com.a.todo.extension.getDelayUntilMorning
import com.a.todo.local.DataStore
import com.a.todo.module.ModuleApp
import com.a.todo.page.AutomaticBackup
import com.a.todo.worker.WorkerBackup
import com.a.todo.worker.WorkerNotification
import com.a.todo.worker.WorkerTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class Application: Application(), Configuration.Provider, KoinComponent {
    private val dataStore by inject<DataStore>()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(ModuleApp.getAllModules())
        }

        setupWorkerTodo(this@Application)
        setupWorkerNotification(this@Application)
        setupWorkerBackup(this@Application)
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

    private fun setupWorkerBackup(context: Context) {
        ProcessLifecycleOwner.get().lifecycleScope.launch (Dispatchers.IO) {
            val workManager = WorkManager.getInstance(context)
            val uniqueWorkerName = "workerAutomaticBackup"

            val delay = when (dataStore.getAutomaticBackup().first()) {
                AutomaticBackup.Off -> null
                AutomaticBackup.Daily -> 24L
                AutomaticBackup.Weekly -> 24L * 7
                AutomaticBackup.Monthly -> 24L * 30
            }

            val workerBackupRequest = PeriodicWorkRequestBuilder<WorkerBackup>(
                delay ?: 0, TimeUnit.HOURS
            ).setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).build()

            when {
                delay != null -> {
                    workManager.enqueueUniquePeriodicWork(
                        uniqueWorkName = uniqueWorkerName,
                        existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
                        request = workerBackupRequest
                    )
                }
                else -> {
                    workManager.cancelUniqueWork(uniqueWorkerName)
                }
            }
        }
    }
}