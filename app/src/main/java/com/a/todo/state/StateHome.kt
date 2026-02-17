package com.a.todo.state

import com.google.firebase.auth.FirebaseAuth

data class StateHome(
    val currentUser: String? = FirebaseAuth.getInstance().currentUser?.uid
)
