package com.jinny.plancast.presentation.payment

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
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
import org.json.JSONArray
import org.json.JSONObject

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

}
