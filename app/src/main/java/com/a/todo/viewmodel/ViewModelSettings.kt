package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.contract.ActionSettings
import com.a.todo.contract.StateSettings
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.repository.ResponseDatabase
import com.a.todo.util.SnackBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelSettings(
    private val repositoryDatabase: RepositoryDatabase,
    private val snackBar: SnackBar
): ViewModel() {
    private val _state = MutableStateFlow(StateSettings())
    val state = _state.onStart {

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateSettings()
    )

    fun onAction(actionSettings: ActionSettings) {
        when (actionSettings) {
            ActionSettings.BottomSheetDeleteAllData -> {
                _state.update { it.copy(bottomSheetDeleteAllData = !it.bottomSheetDeleteAllData) }
            }
            ActionSettings.ButtonDeleteAllData -> {
                buttonDeleteAllData()
            }
            ActionSettings.AboutAppClick -> {
                aboutAppClick()
            }
        }
    }

    private fun buttonDeleteAllData() {
        viewModelScope.launch {
            repositoryDatabase.deleteAllTodo().collect {
                when (it) {
                    is ResponseDatabase.Success -> snackBar.showSnackBar(it.messageSuccess)
                    is ResponseDatabase.Failed -> snackBar.showSnackBar(it.messageFailed)
                }
            }
        }
    }

    private fun aboutAppClick() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            if (currentTime - _state.value.aboutAppLastClick > 1000) {
                _state.update { it.copy(aboutAppClickCount = 1) }
            } else {
                _state.update { it.copy(aboutAppClickCount = it.aboutAppClickCount + 1) }
            }

            _state.update { it.copy(aboutAppLastClick = currentTime) }

            when (_state.value.aboutAppClickCount) {
                3 -> snackBar.showSnackBar("Theres nothing here")
                4 -> snackBar.showSnackBar("Are you still trying ?")
                5 -> snackBar.showSnackBar("Like what i said before")
            }
        }
    }
}