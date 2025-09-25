package com.jinny.plancast.presentation.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.domain.transferUseCase.TransferMoneyUseCase
import com.jinny.plancast.presentation.BaseViewModel
import com.jinny.plancast.presentation.login.LoginState
import com.jinny.plancast.presentation.todo.detail.ToDoDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransferViewModel(
    private val transferMoneyUseCase: TransferMoneyUseCase
) : BaseViewModel() {

    private val _transferResult = MutableLiveData<String>()
    val transferResult: LiveData<String> = _transferResult

    private val _passwordCheck = MutableStateFlow<Boolean>(false)
    val passwordCheck: StateFlow<Boolean> = _passwordCheck

    private var _transferLiveData = MutableLiveData<ToDoDetailState>(ToDoDetailState.UnInitialized)
    val transferLiveData: LiveData<ToDoDetailState> = _transferLiveData

    fun transferMoney(recipient: String, amount: Int) {
        viewModelScope.launch {
            try {
                val result = transferMoneyUseCase.execute(recipient, amount)
                _transferResult.postValue(result.message)
            } catch (e: Exception) {
                _transferResult.postValue("송금 실패: ${e.message}")
            }
        }
    }

    fun goToPassword() {
        // 비밀번호 화면으로 이동하는 로직 구현
        _passwordCheck.value = true
    }

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
