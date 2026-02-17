package com.a.todo.application

import android.app.Application
import com.a.todo.module.ModuleApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(ModuleApp.getAllModules())
        }
    }
}