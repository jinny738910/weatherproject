package com.jinny.plancast.presentation.financial.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.domain.productUseCase.CreateProductsUseCase
import com.jinny.plancast.domain.productUseCase.DeleteProductsUseCase
import com.jinny.plancast.domain.productUseCase.GetProductsByIdUseCase
import com.jinny.plancast.domain.productUseCase.GetProductsUseCase
import com.jinny.plancast.domain.productUseCase.UpdateProductsUseCase
import com.jinny.plancast.domain.repository.BackendRepository
import com.jinny.plancast.domain.repository.ProductRepository
import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val backendRepository: BackendRepository,
    private val createProductUsecase: CreateProductsUseCase,
    private val deleteProductsUseCase: DeleteProductsUseCase,
    private val getProductsByIdUseCase: GetProductsByIdUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val updateProductsUseCase: UpdateProductsUseCase
) : BaseViewModel() {


    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message // UI가 관찰할 LiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

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

    fun loadHelloMessage() {
        // Coroutine을 사용하여 비동기 작업을 ViewModel 범위에서 실행
        viewModelScope.launch {
            _isLoading.value = true // 로딩 시작
            _error.value = null     // 오류 초기화

            val result = backendRepository.fetchHelloMessage()
            result.onSuccess { message ->
                Log.d("PaymentViewModel", "loadHelloMessage: $message")
                _message.value = message // 성공 시 메시지 설정
            }.onFailure { exception ->
                Log.d("PaymentViewModel", "loadHelloMessage error: $message")
                _error.value = exception.message // 실패 시 오류 메시지 설정
            }
            _isLoading.value = false // 로딩 종료
        }
    }

    fun getAllProducts() {
        // Coroutine을 사용하여 비동기 작업을 ViewModel 범위에서 실행
        viewModelScope.launch {
            _isLoading.value = true // 로딩 시작
            _error.value = null     // 오류 초기화

            val result = getProductsUseCase()
            result.onSuccess { message ->
                Log.d("PaymentViewModel", "getAllProducts : $message")
            }.onFailure { exception ->
                Log.d("PaymentViewModel", "getAllProducts error: $message")
                _error.value = exception.message // 실패 시 오류 메시지 설정
            }
            _isLoading.value = false // 로딩 종료
        }
    }

    fun getProductById(id: Long) {
        // Coroutine을 사용하여 비동기 작업을 ViewModel 범위에서 실행
        viewModelScope.launch {
            _isLoading.value = true // 로딩 시작
            _error.value = null     // 오류 초기화

            val result = getProductsByIdUseCase(id)
            result.onSuccess { message ->
                Log.d("PaymentViewModel", "getProductById: $message")
            }.onFailure { exception ->
                Log.d("PaymentViewModel", "getProductById error: $message")
                _error.value = exception.message // 실패 시 오류 메시지 설정
            }
            _isLoading.value = false // 로딩 종료
        }
    }

    fun createProduct(product: Product) {
        // Coroutine을 사용하여 비동기 작업을 ViewModel 범위에서 실행
        viewModelScope.launch {
            _isLoading.value = true // 로딩 시작
            _error.value = null     // 오류 초기화

            val result = createProductUsecase(product)
            result.onSuccess { message ->
                Log.d("PaymentViewModel", "create Product: $message")
            }.onFailure { exception ->
                Log.d("PaymentViewModel", "create Product error: $message")
                _error.value = exception.message // 실패 시 오류 메시지 설정
            }
            _isLoading.value = false // 로딩 종료
        }
    }

    fun updateProduct(id: Long, product: Product) {
        // Coroutine을 사용하여 비동기 작업을 ViewModel 범위에서 실행
        viewModelScope.launch {
            _isLoading.value = true // 로딩 시작
            _error.value = null     // 오류 초기화

            val result = updateProductsUseCase(id, product)
            result.onSuccess { message ->
                Log.d("PaymentViewModel", "updateProduct: $message")
            }.onFailure { exception ->
                Log.d("PaymentViewModel", "updateProduct error: $message")
                _error.value = exception.message // 실패 시 오류 메시지 설정
            }
            _isLoading.value = false // 로딩 종료
        }
    }

    fun deleteProduct(id: Long) {
        // Coroutine을 사용하여 비동기 작업을 ViewModel 범위에서 실행
        viewModelScope.launch {
            _isLoading.value = true // 로딩 시작
            _error.value = null     // 오류 초기화

            val result = deleteProductsUseCase(id)

            _isLoading.value = false // 로딩 종료
        }
    }


}
