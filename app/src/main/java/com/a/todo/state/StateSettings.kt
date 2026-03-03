package com.a.todo.state

import com.google.firebase.auth.FirebaseUser

data class StateSettings(
    val currentUser: FirebaseUser? = null
)
