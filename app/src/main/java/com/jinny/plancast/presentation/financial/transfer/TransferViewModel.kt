package com.jinny.plancast.presentation.financial.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.domain.usecase.transferUseCase.ExecuteTransferUseCase
import com.jinny.plancast.domain.usecase.transferUseCase.RegisterBillingKeyUseCase
import com.jinny.plancast.presentation.BaseViewModel
import com.jinny.plancast.presentation.todo.detail.ToDoDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransferViewModel(
    private val executeTransferUseCase: com.jinny.plancast.domain.usecase.transferUseCase.ExecuteTransferUseCase,
    private val registerBillingKeyUseCase: com.jinny.plancast.domain.usecase.transferUseCase.RegisterBillingKeyUseCase
) : BaseViewModel() {

    private val _transferResult = MutableLiveData<String>()
    val transferResult: LiveData<String> = _transferResult

    private val _passwordCheck = MutableStateFlow<Boolean>(false)
    val passwordCheck: StateFlow<Boolean> = _passwordCheck

    private var _transferLiveData = MutableLiveData<ToDoDetailState>(ToDoDetailState.UnInitialized)
    val transferLiveData: LiveData<ToDoDetailState> = _transferLiveData

    private val customerKey = "side_project_user_${System.currentTimeMillis()}"
    /**
     * 토스 인증 후 받은 authKey로 빌링키 발급을 요청합니다.
     */
    fun registerBillingKey(authKey: String) {
        viewModelScope.launch {
            // 1. 로딩 상태 시작
//            _uiState.update { it.copy(isRegistering = true) }

            registerBillingKeyUseCase(authKey, customerKey)
                .onSuccess {
                    // 2. 성공 시, 등록 성공 상태로 변경
//                    _uiState.update {
//                        it.copy(isRegistering = false, isRegistrationSuccess = true)
//                    }
                }
                .onFailure { error ->
                    // 3. 실패 시, 에러 메시지 표시
//                    _uiState.update {
//                        it.copy(isRegistering = false, errorMessage = error.message)
//                    }
                }
        }
    }

    /**
     * 등록된 결제 수단으로 결제(송금)를 실행합니다.
     */
    fun makeTransfer(amount: Long, orderName: String) {
        viewModelScope.launch {
//            _uiState.update { it.copy(isPaying = true) }

            executeTransferUseCase(amount, orderName, customerKey)
                .onSuccess { result ->
//                    _uiState.update {
//                        it.copy(isPaying = false, paymentResult = result)
//                    }
                }
                .onFailure { error ->
//                    _uiState.update {
//                        it.copy(isPaying = false, errorMessage = error.message)
//                    }
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
