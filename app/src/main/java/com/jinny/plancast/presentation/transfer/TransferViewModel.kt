package com.jinny.plancast.presentation.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.domain.todoUseCase.DeleteToDoItemUseCase
import com.jinny.plancast.domain.todoUseCase.GetToDoItemUseCase
import com.jinny.plancast.domain.todoUseCase.InsertToDoUseCase
import com.jinny.plancast.domain.todoUseCase.UpdateToDoUseCase
import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TransferViewModel(
    var id: Long = -1,
) : BaseViewModel() {


    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    override fun fetchData() = viewModelScope.launch {
//        when (weatherMode) {
//            weatherMode.WRITE -> {
//                _weatherLiveData.postValue(WeatherState.Write)
//            }
//            weatherMode.DETAIL -> {
//                _weatherLiveData.postValue(WeatherState.Loading)
//                try {
//                    getToDoItemUseCase(id)?.let {
//                        _weatherLiveData.postValue(WeatherState.Success(it))
//                    } ?: kotlin.run {
//                        _weatherLiveData.postValue(WeatherState.Error)
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    _weatherLiveData.postValue(WeatherState.Error)
//                }
//            }
    }

    fun onOpenActivityClick() {
        viewModelScope.launch {
            _navigationEvent.emit(Unit) // Activity에 신호 보내기
        }
    }
}
