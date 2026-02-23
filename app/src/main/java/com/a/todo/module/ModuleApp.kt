package com.a.todo.module

import com.a.todo.services.FirebaseAuth
import com.a.todo.util.SnackBar
import com.a.todo.viewmodel.ViewModelAddTodo
import com.a.todo.viewmodel.ViewModelHome
import com.a.todo.viewmodel.ViewModelMain
import com.a.todo.viewmodel.ViewModelSignIn
import com.a.todo.viewmodel.ViewModelSignUp
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object ModuleApp {
    private val moduleViewModel = module {
        viewModelOf(::ViewModelMain)
        viewModelOf(::ViewModelSignIn)
        viewModelOf(::ViewModelSignUp)
        viewModelOf(::ViewModelHome)
        viewModelOf(::ViewModelAddTodo)
    }

    private val moduleServices = module {
        singleOf(::FirebaseAuth)
    }

    private val moduleUtils = module {
        singleOf(::SnackBar)
    }

    fun getAllModules() = listOf(
        moduleViewModel,
        moduleServices,
        moduleUtils
    )
}