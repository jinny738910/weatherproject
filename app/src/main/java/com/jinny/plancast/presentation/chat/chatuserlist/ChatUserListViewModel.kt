package com.jinny.plancast.presentation.chat.chatuserlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jinny.plancast.presentation.BaseViewModel
import com.jinny.plancast.presentation.chat.chatview.ChatViewItem
import kotlinx.coroutines.*

class ChatUserListViewModel(
//    private val getToDoListUseCase: GetToDoListUseCase,
//    private val updateToDoUseCase: UpdateToDoUseCase,
//    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
): BaseViewModel() {

    private var _toChatListListLiveData = MutableLiveData<ChatViewItem>()
    val ChatListLiveData: LiveData<ChatViewItem> = _toChatListListLiveData

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun fetchData(): Job = viewModelScope.launch {
//        _toChatListListLiveData.postValue(ToDoListState.Loading)
//        val toDoList = getToDoListUseCase()
//        _toChatListListLiveData.postValue(ToDoListState.Suceess(toDoList))
    }




}
