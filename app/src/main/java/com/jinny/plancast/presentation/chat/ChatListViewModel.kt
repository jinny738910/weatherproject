package com.jinny.plancast.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.presentation.BaseViewModel
import com.jinny.plancast.presentation.todo.list.ToDoListState
import kotlinx.coroutines.*

class ChatListViewModel(
//    private val getToDoListUseCase: GetToDoListUseCase,
//    private val updateToDoUseCase: UpdateToDoUseCase,
//    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
): BaseViewModel() {

    private var _toChatListListLiveData = MutableLiveData<ChatListItem>()
    val ChatListLiveData: LiveData<ChatListItem> = _toChatListListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
//        _toChatListListLiveData.postValue(ToDoListState.Loading)
//        val toDoList = getToDoListUseCase()
//        _toChatListListLiveData.postValue(ToDoListState.Suceess(toDoList))
    }




}
