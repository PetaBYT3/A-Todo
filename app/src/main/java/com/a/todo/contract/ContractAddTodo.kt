package com.a.todo.contract

import com.a.todo.enumclass.TodoImportance

sealed interface ActionAddTodo {
    data class RadioButtonTodoImportance(val todoImportance: TodoImportance): ActionAddTodo
    data class ButtonGroupTodoImportance(val todoImportance: String): ActionAddTodo
    data object ButtonIncreaseTodoDay: ActionAddTodo
    data object ButtonDecreaseTodoDay: ActionAddTodo
    data class TextFieldTodoTitle(val todoTitle: String): ActionAddTodo
    data class TextFieldTodoContent(val todoContent: String): ActionAddTodo
    data object ButtonSaveTodo: ActionAddTodo
}

data class StateAddTodo(
    val radioButtonTodoImportance: TodoImportance? = null,
    val buttonGroupTodoImportance: String = "",
    val textTodoDay: Int = 1,
    val textFieldTodoTitle: String = "",
    val textFieldTodoContent: String = "",
)