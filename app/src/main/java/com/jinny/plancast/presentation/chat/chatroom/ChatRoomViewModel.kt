package com.jinny.plancast.presentation.chat.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
//    private val getToDoListUseCase: GetToDoListUseCase,
//    private val updateToDoUseCase: UpdateToDoUseCase,
//    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
): BaseViewModel() {

    private var _toDoListLiveData = MutableLiveData<MessageItem>()
    val toDoListLiveData: LiveData<MessageItem> = _toDoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {

    }

    fun updateEntity(toDoEntity: MessageItem) = viewModelScope.launch {

    }

    fun deleteAll() = viewModelScope.launch {

    }

}
