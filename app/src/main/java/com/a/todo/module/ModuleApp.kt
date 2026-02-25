package com.a.todo.module

import androidx.room.Room
import com.a.todo.local.Database
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.services.FirebaseAuth
import com.a.todo.util.SnackBar
import com.a.todo.viewmodel.ViewModelAddTodo
import com.a.todo.viewmodel.ViewModelHome
import com.a.todo.viewmodel.ViewModelMain
import com.a.todo.viewmodel.ViewModelSignIn
import com.a.todo.viewmodel.ViewModelSignUp
import com.a.todo.viewmodel.ViewModelToday
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object ModuleApp {
    private val moduleViewModel = module {
        viewModelOf(::ViewModelMain)
        viewModelOf(::ViewModelSignIn)
        viewModelOf(::ViewModelSignUp)
        viewModelOf(::ViewModelHome)
        viewModelOf(::ViewModelToday)
        viewModelOf(::ViewModelAddTodo)
    }

    private val moduleServices = module {
        singleOf(::FirebaseAuth)
    }

    private val moduleLocalDatabase = module {
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

    fun getAllModules() = listOf(
        moduleViewModel,
        moduleServices,
        moduleLocalDatabase,
        moduleUtils
    )
}