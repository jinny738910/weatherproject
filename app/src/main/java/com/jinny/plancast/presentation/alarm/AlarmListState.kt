package com.jinny.plancast.presentation.alarm

import com.jinny.plancast.data.local.entity.ToDoEntity


sealed class AlarmListState {

    object UnInitialized: AlarmListState()

    object Loading: AlarmListState()

    data class Success(
        val toDoItem: ToDoEntity
    ): AlarmListState()

    object Delete: AlarmListState()

    object Modify: AlarmListState()

    data class Error(
        val message: String
    ): AlarmListState()

    object Write: AlarmListState()
}
