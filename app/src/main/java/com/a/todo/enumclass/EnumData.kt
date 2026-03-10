package com.a.todo.enumclass

enum class TodoImportance(
    val value: String
) {
    Low("Low"),
    Medium("Medium"),
    High("High")
}

enum class TodoStatus {
    Todo,
    Done,
    Expired
}