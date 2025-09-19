package com.jinny.plancast.presentation.todo.list

import com.jinny.plancast.data.local.entity.ToDoEntity

sealed class ToDoListState {

    object UnInitialized: ToDoListState()

    object Loading: ToDoListState()

    data class Suceess(
        val toDoList: List<ToDoEntity>
    ): ToDoListState()

    data class Error(
        val message: String
    ): ToDoListState()

}
