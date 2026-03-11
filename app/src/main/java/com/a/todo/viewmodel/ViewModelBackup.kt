package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.contract.ActionBackup
import com.a.todo.contract.StateBackup
import com.a.todo.local.Dao
import com.a.todo.local.DataStore
import com.a.todo.page.AutomaticBackup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelBackup(
    private val dao: Dao,
    private val dataStore: DataStore
): ViewModel() {
    private val _state = MutableStateFlow(StateBackup())
    val state = _state.onStart {
        getLocalTodoSize()
        getDataStore()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateBackup()
    )

    private fun getLocalTodoSize() {
        viewModelScope.launch {
            dao.getAllTodo().collect { result ->
                _state.update { it.copy(localTodoSize = result.size) }
            }
        }
    }

    private fun getDataStore() {
        viewModelScope.launch {
            dataStore.getAutomaticBackup().collect { result ->
                _state.update { it.copy(automaticBackup = result) }
            }
        }
    }

    fun onAction(actionBackup: ActionBackup) {
        when (actionBackup) {
            is ActionBackup.SetAutomaticBackup -> {
                setAutomaticBackup(actionBackup.automaticBackup)
            }
            ActionBackup.BottomSheetAutomaticBackup -> {
                _state.update { it.copy(bottomSheetAutomaticBackup = !it.bottomSheetAutomaticBackup) }
            }
            ActionBackup.ButtonBackup -> TODO()
        }
    }

    private fun setAutomaticBackup(automaticBackup: AutomaticBackup) {
        viewModelScope.launch {
            dataStore.setAutomaticBackup(automaticBackup)
        }
    }
}