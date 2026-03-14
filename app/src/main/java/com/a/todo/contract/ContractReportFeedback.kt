package com.a.todo.contract

sealed interface ActionReportFeedback {
    data object ButtonSend: ActionReportFeedback
}

data class StateReportFeedback(
    val textFieldReportAndFeedback: String = ""
)