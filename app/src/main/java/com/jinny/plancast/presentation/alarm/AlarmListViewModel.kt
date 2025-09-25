package com.jinny.plancast.presentation.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.domain.todoUseCase.DeleteAllToDoItemUseCase
import com.jinny.plancast.domain.todoUseCase.GetToDoListUseCase
import com.jinny.plancast.domain.todoUseCase.UpdateToDoUseCase
import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.*

class AlarmListViewModel(
//    private val getToDoListUseCase: GetToDoListUseCase,
//    private val updateToDoUseCase: UpdateToDoUseCase,
//    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
): BaseViewModel() {

    private var _toDoListLiveData = MutableLiveData<AlarmListState>(AlarmListState.UnInitialized)
    val toDoListLiveData: LiveData<AlarmListState> = _toDoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        try {
            _toDoListLiveData.postValue(AlarmListState.Loading)
//            val toDoListLiveDataDoList = getToDoListUseCase()
//            _toDoListLiveDataiveData.postValue(AlarmListState.Suceess(toDoList))
        } catch (e: Exception) {
            _toDoListLiveData.postValue(AlarmListState.Error(e.message ?: "데이터를 불러오는데 실패했습니다"))
        }
    }

    fun updateEntity(toDoEntity: ToDoEntity) = viewModelScope.launch {
        try {
//            updateToDoUseCase(toDoEntity)
        } catch (e: Exception) {
            _toDoListLiveData.postValue(AlarmListState.Error(e.message ?: "업데이트에 실패했습니다"))
        }
    }

    fun deleteAll() = viewModelScope.launch {
        try {
            _toDoListLiveData.postValue(AlarmListState.Loading)
//            deleteAllToDoItemUseCase()
//            val toDoEntityDoList = getToDoListUseCase()
//            _toDoListLiveDataLiveData.postValue(AlarmListState.Suceess(toDoList))
        } catch (e: Exception) {
            _toDoListLiveData.postValue(AlarmListState.Error(e.message ?: "삭제에 실패했습니다"))
        }
    }


}
