package com.jinny.plancast.presentation.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.*

class SettingViewModel(
): BaseViewModel() {

    private var _toDoListLiveData = MutableLiveData<SettingListState>(SettingListState.UnInitialized)
    val toDoListLiveData: LiveData<SettingListState> = _toDoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        try {
            _toDoListLiveData.postValue(SettingListState.Loading)
//            val toDoListLiveDataDoList = getToDoListUseCase()
//            _toDoListLiveDataiveData.postValue(AlarmListState.Suceess(toDoList))
        } catch (e: Exception) {
            _toDoListLiveData.postValue(SettingListState.Error(e.message ?: "데이터를 불러오는데 실패했습니다"))
        }
    }

    fun updateEntity(toDoEntity: ToDoEntity) = viewModelScope.launch {
        try {
//            updateToDoUseCase(toDoEntity)
        } catch (e: Exception) {
            _toDoListLiveData.postValue(SettingListState.Error(e.message ?: "업데이트에 실패했습니다"))
        }
    }

    fun deleteAll() = viewModelScope.launch {
        try {
            _toDoListLiveData.postValue(SettingListState.Loading)
//            deleteAllToDoItemUseCase()
//            val toDoEntityDoList = getToDoListUseCase()
//            _toDoListLiveDataLiveData.postValue(AlarmListState.Suceess(toDoList))
        } catch (e: Exception) {
            _toDoListLiveData.postValue(SettingListState.Error(e.message ?: "삭제에 실패했습니다"))
        }
    }


}
