package com.a.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.todo.local.Dao
import com.a.todo.repository.RepositoryDatabase
import com.a.todo.repository.ResponseDatabase
import com.a.todo.state.StateToday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelToday(
    private val dao: Dao,
    private val repositoryDatabase: RepositoryDatabase
): ViewModel() {
    private val _state = MutableStateFlow(StateToday())
    val state = _state.onStart {
        getTodoToday()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StateToday()
    )

    private fun getTodoToday() {
        viewModelScope.launch {
//            repositoryDatabase.getTodoToday().collect { result ->
//                when (result) {
//                    is ResponseDatabase.Success -> {
//                        _state.update { it.copy(todoToday = result.listTodo) }
//                    }
//                    is ResponseDatabase.Failed -> {
//
//                    }
//                }
//            }
            dao.getAllTodo().collect { result ->
                _state.update { it.copy(todoToday = result) }
            }
        }
    }
}