package com.a.todo.event

import com.a.todo.enumclass.TodoImportance

sealed interface EventAddTodo {
    data class ButtonGroupTodoImportance(val todoImportance: String): EventAddTodo
    data class TextFieldTodoTitle(val todoTitle: String): EventAddTodo
    data class TextFieldTodoContent(val todoContent: String): EventAddTodo
}