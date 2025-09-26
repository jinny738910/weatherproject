package com.jinny.plancast.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.*

class ChatRoomViewModel(
//    private val getToDoListUseCase: GetToDoListUseCase,
//    private val updateToDoUseCase: UpdateToDoUseCase,
//    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
): BaseViewModel() {

    private var _toDoListLiveData = MutableLiveData<ChatRoomItem>()
    val toDoListLiveData: LiveData<ChatRoomItem> = _toDoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {

    }

    fun updateEntity(toDoEntity: ChatRoomItem) = viewModelScope.launch {

    }

    fun deleteAll() = viewModelScope.launch {

    }

}
