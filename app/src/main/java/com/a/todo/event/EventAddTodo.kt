package com.a.todo.event

import com.a.todo.enumclass.TodoImportance

sealed interface EventAddTodo {
    data class ButtonGroupTodoImportance(val todoImportance: TodoImportance): EventAddTodo
    data class TextFieldTodoTitle(val todoTitle: String): EventAddTodo
}