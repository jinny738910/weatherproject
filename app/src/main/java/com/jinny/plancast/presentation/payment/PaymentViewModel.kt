package com.jinny.plancast.presentation.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.domain.todoUseCase.DeleteToDoItemUseCase
import com.jinny.plancast.domain.todoUseCase.GetToDoItemUseCase
import com.jinny.plancast.domain.todoUseCase.InsertToDoUseCase
import com.jinny.plancast.domain.todoUseCase.UpdateToDoUseCase
import com.jinny.plancast.presentation.BaseViewModel
import com.jinny.plancast.presentation.login.LoginState
import com.jinny.plancast.presentation.login.LoginViewModel
import com.jinny.plancast.presentation.todo.list.ToDoListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    var id: Long = -1,
) : BaseViewModel() {


    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog

    private var _loginLiveData = MutableLiveData<LoginState>(LoginState.Idle)
    val loginLiveData: LiveData<LoginState> = _loginLiveData


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
//            _paymentStateEvent.emit() // Activity에 신호 보내기
        }
    }
}
