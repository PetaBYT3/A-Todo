package com.a.todo.contract

sealed interface ActionReportFeedback {
    data class TextFieldReportAndFeedback(val reportAndFeedback: String): ActionReportFeedback
    data object ButtonSend: ActionReportFeedback
}

data class StateReportFeedback(
    val textFieldReportAndFeedback: String = ""
)