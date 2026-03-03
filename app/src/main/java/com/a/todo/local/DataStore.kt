package com.a.todo.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appDataStore")

class DataStore(
    private val context: Context
) {
    private val isAnonymousWarnVisible = booleanPreferencesKey("isAnonymousWarnVisible")

    fun anonymousWarnVisibility(): Flow<Boolean> = context.dataStore.data.map {
        it[isAnonymousWarnVisible] ?: true
    }.flowOn(Dispatchers.IO)

    suspend fun disableAnonymousWarn() = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[isAnonymousWarnVisible] = false }
    }
}