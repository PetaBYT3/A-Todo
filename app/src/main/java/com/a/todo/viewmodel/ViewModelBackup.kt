package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.contract.ActionBackup
import com.a.todo.contract.StateBackup
import com.a.todo.local.Dao
import com.a.todo.local.DataStore
import com.a.todo.page.AutomaticBackup
import com.a.todo.services.FirebaseFirestore
import com.a.todo.services.ResponseFirestore
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelBackup(
    private val dao: Dao,
    private val dataStore: DataStore,
    private val firebaseFirestore: FirebaseFirestore,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateBackup())
    val state = _state.onStart {
        getLocalTodoSize()
        getTodoCloudDescription()
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

    private fun getTodoCloudDescription() {
        viewModelScope.launch {
            firebaseFirestore.getTodoCloudDescription().collect { result ->
                _state.update { it.copy(todoCloudDescription = result) }
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
            ActionBackup.ButtonBackup -> {
                buttonBackup()
            }
            ActionBackup.BottomSheetClearDataOnCloud -> {
                _state.update { it.copy(bottomSheetClearDataOnCloud = !it.bottomSheetClearDataOnCloud) }
            }
            ActionBackup.ButtonClearDataOnCloud -> {
                buttonClearDataOnCloud()
            }
        }
    }

    private fun setAutomaticBackup(automaticBackup: AutomaticBackup) {
        viewModelScope.launch {
            dataStore.setAutomaticBackup(automaticBackup)
        }
    }

    private fun buttonBackup() {
        viewModelScope.launch {
            firebaseFirestore.backupLocalToFirestore().onStart {
                _state.update { it.copy(isButtonBackupNowLoading = true) }
            }.onCompletion {
                _state.update { it.copy(isButtonBackupNowLoading = false) }
            }.collect {
                when (it) {
                    is ResponseFirestore.Success -> snackBar.showSnackBar(it.messageSuccess)
                    is ResponseFirestore.Failed -> snackBar.showSnackBar(it.messageFailed)
                }
            }
        }
    }

    private fun buttonClearDataOnCloud() {
        viewModelScope.launch {
            firebaseFirestore.clearDataOnFirestore().onStart {
                _state.update { it.copy(isButtonClearDataOnCloudLoading = true) }
            }.onCompletion {
                _state.update { it.copy(isButtonClearDataOnCloudLoading = false) }
            }.collect {
                when (it) {
                    is ResponseFirestore.Success -> snackBar.showSnackBar(it.messageSuccess)
                    is ResponseFirestore.Failed -> snackBar.showSnackBar(it.messageFailed)
                }
            }
        }
    }
}