package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.contract.ActionReportFeedback
import com.a.todo.contract.StateReportFeedback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ViewModelReportFeedback(

): ViewModel() {
    private val _state = MutableStateFlow(StateReportFeedback())
    val state = _state.onStart {

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateReportFeedback()
    )

    fun onAction(actionReportFeedback: ActionReportFeedback) {
        when (actionReportFeedback) {
            is ActionReportFeedback.TextFieldReportAndFeedback -> {
                _state.update { it.copy(textFieldReportAndFeedback = actionReportFeedback.reportAndFeedback) }
            }
            ActionReportFeedback.ButtonSend -> TODO()
        }
    }
}