package com.a.todo.module

import androidx.room.Room
import com.a.todo.local.DataStore
import com.a.todo.local.Database
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.services.FirebaseAuth
import com.a.todo.util.SnackBar
import com.a.todo.viewmodel.ViewModelAddTodo
import com.a.todo.viewmodel.ViewModelAll
import com.a.todo.viewmodel.ViewModelEmailVerification
import com.a.todo.viewmodel.ViewModelHome
import com.a.todo.viewmodel.ViewModelMain
import com.a.todo.viewmodel.ViewModelSettings
import com.a.todo.viewmodel.ViewModelSignIn
import com.a.todo.viewmodel.ViewModelSignUp
import com.a.todo.viewmodel.ViewModelToday
import com.a.todo.viewmodel.ViewModelTomorrow
import com.a.todo.worker.WorkerNotification
import com.a.todo.worker.WorkerTodo
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object ModuleApp {
    private val moduleViewModel = module {
        viewModelOf(::ViewModelMain)
        viewModelOf(::ViewModelSignIn)
        viewModelOf(::ViewModelSignUp)
        viewModelOf(::ViewModelEmailVerification)
        viewModelOf(::ViewModelHome)
        viewModelOf(::ViewModelToday)
        viewModelOf(::ViewModelTomorrow)
        viewModelOf(::ViewModelAll)
        viewModelOf(::ViewModelAddTodo)
        viewModelOf(::ViewModelSettings)
    }

    private val moduleServices = module {
        singleOf(::FirebaseAuth)
    }

    private val moduleLocalDatabase = module {
        singleOf(::DataStore)
        single {
            Room.databaseBuilder(
                context = androidContext(),
                klass = Database::class.java,
                name = "A-Todo-Database.db"
            ).fallbackToDestructiveMigration(false).build()
        }
        single { get<Database>().dao() }
        singleOf(::RepositoryDatabase)
    }

    private val moduleUtils = module {
        singleOf(::SnackBar)
    }

    private val moduleWorker = module {
        workerOf(::WorkerTodo)
        workerOf(::WorkerNotification)
    }

    fun getAllModules() = listOf(
        moduleViewModel,
        moduleServices,
        moduleLocalDatabase,
        moduleUtils,
        moduleWorker
    )
}