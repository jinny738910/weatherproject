package com.jinny.plancast.presentation.setting

import com.jinny.plancast.data.local.entity.ToDoEntity


sealed class SettingListState {

    object UnInitialized: SettingListState()

    object Loading: SettingListState()

    data class Success(
        val toDoItem: ToDoEntity
    ): SettingListState()

    object Delete: SettingListState()

    object Modify: SettingListState()

    data class Error(
        val message: String
    ): SettingListState()

    object Write: SettingListState()
}
