package com.jinny.plancast.presentation.todo.detail

import com.jinny.plancast.data.local.entity.ToDoEntity


sealed class ToDoDetailState {

    object UnInitialized: ToDoDetailState()

    object Loading: ToDoDetailState()

    data class Success(
        val toDoItem: ToDoEntity
    ): ToDoDetailState()

    object Delete: ToDoDetailState()

    object Modify: ToDoDetailState()

    data class Error(
        val message: String
    ): ToDoDetailState()

    object Write: ToDoDetailState()
}
