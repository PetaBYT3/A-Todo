package com.a.todo.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface RoutePage: NavKey {
    @Serializable
    data object PageSignIn: RoutePage, NavKey

    @Serializable
    data object PageSignUp: RoutePage, NavKey

    @Serializable
    data object PageHome: RoutePage, NavKey
}