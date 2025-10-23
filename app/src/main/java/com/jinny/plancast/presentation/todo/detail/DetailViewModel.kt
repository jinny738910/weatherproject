package com.jinny.plancast.presentation.todo.detail

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.domain.usecase.todoUseCase.DeleteToDoItemUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.GetToDoItemUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.InsertToDoUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.UpdateToDoUseCase
import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel(
    var detailMode: DetailMode,
    var id: Long = -1,
    private val getToDoItemUseCase: com.jinny.plancast.domain.usecase.todoUseCase.GetToDoItemUseCase,
    private val deleteToDoItemUseCase: com.jinny.plancast.domain.usecase.todoUseCase.DeleteToDoItemUseCase,
    private val updateToDoUseCase: com.jinny.plancast.domain.usecase.todoUseCase.UpdateToDoUseCase,
    private val insertToDoUseCase: com.jinny.plancast.domain.usecase.todoUseCase.InsertToDoUseCase
) : BaseViewModel() {

    private var _toDoDetailLiveData = MutableLiveData<ToDoDetailState>(ToDoDetailState.UnInitialized)
    val toDoDetailLiveData: LiveData<ToDoDetailState> = _toDoDetailLiveData

    override fun fetchData() = viewModelScope.launch {
        when (detailMode) {
            DetailMode.WRITE -> {
                _toDoDetailLiveData.postValue(ToDoDetailState.Write)
            }
            DetailMode.DETAIL -> {
                _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
                try {
                    getToDoItemUseCase(id)?.let {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Success(it))
                        Log.d("DetailViewModel", "fetch detail mode : isClimate, isLocation, isFinancial: $it.isClimate, $it.sLocation, $it.isFinancial")

                    } ?: kotlin.run {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Error("데이터를 찾을 수 없습니다"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error(e.message ?: "데이터를 불러오는데 실패했습니다"))
                }
            }
        }
    }

    fun deleteToDo() = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
        try {
            deleteToDoItemUseCase(id)
            _toDoDetailLiveData.postValue(ToDoDetailState.Delete)
        } catch (e: Exception) {
            e.printStackTrace()
            _toDoDetailLiveData.postValue(ToDoDetailState.Error(e.message ?: "삭제에 실패했습니다"))
        }
    }

    fun setModifyMode() = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Modify)
    }

    fun writeToDo(
        title: String,
        date: String,
        destination: String,
        description: String,
        image: Uri?,
        isClimate: Boolean,
        isLocation: Boolean,
        isFinancial: Boolean,
        isRepeat: Boolean,
        isLock: Boolean,
        hasCompleted: Boolean
    ) = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
        when (detailMode) {
            DetailMode.WRITE -> {
                try {
                    val toDoEntity = ToDoEntity(
                        title = title,
                        date = date,
                        destination = destination,
                        description = description,
                        image = image.toString(),
                        hasCompleted = hasCompleted,
                        isClimate = isClimate,
                        isLocation = isLocation,
                        isFinancial = isFinancial,
                        isRepeat = isRepeat,
                        isLock = isLock
                    )
                    id = insertToDoUseCase(toDoEntity)
                    _toDoDetailLiveData.postValue(ToDoDetailState.Success(toDoEntity))
                    Log.d("DetailViewModel", "write mode : isClimate, isLocation, isFinancial: $isClimate, $isLocation, $isFinancial")
                    detailMode = DetailMode.DETAIL
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error(e.message ?: "저장에 실패했습니다"))
                }
            }
            DetailMode.DETAIL -> {
                try {
                    getToDoItemUseCase(id)?.let {
                        val updateToDoEntity = it.copy(title = title, description = description, date = date,
                            destination = destination, image = image.toString(), isClimate = isClimate,
                            isLocation = isLocation, isFinancial = isFinancial, isRepeat = isRepeat,
                            isLock = isLock, hasCompleted = hasCompleted)
                        updateToDoUseCase(updateToDoEntity)
                        _toDoDetailLiveData.postValue(ToDoDetailState.Success(updateToDoEntity))
                        Log.d("DetailViewModel", "detail mode : isClimate, isLocation, isFinancial: $isClimate, $isLocation, $isFinancial")
                    } ?: kotlin.run {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Error("업데이트할 데이터를 찾을 수 없습니다"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error(e.message ?: "업데이트에 실패했습니다"))
                }
            }
        }
    }

}
