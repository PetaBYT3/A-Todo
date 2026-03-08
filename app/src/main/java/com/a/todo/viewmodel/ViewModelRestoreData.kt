package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.event.ActionRestoreData
import com.a.todo.state.StateRestoreData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewModelRestoreData(

): ViewModel() {
    private val _state = MutableStateFlow(StateRestoreData())
    val state = _state.onStart {

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateRestoreData()
    )

    fun onAction(actionRestoreData: ActionRestoreData) {
        when (actionRestoreData) {
            ActionRestoreData.ButtonRestoreData -> {
                buttonRestoreData()
            }
        }
    }

    private fun buttonRestoreData() {
        viewModelScope.launch {

        }
    }
}