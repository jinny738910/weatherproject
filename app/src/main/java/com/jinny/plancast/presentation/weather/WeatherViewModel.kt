package com.jinny.plancast.presentation.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.domain.model.WeatherInfo
import com.jinny.plancast.domain.weatherUseCase.GetShortTermForecastUseCase
import com.jinny.plancast.domain.weatherUseCase.GetUltraShortTermForecastUseCase
import com.jinny.plancast.presentation.BaseViewModel
import com.jinny.plancast.presentation.todo.detail.ToDoDetailState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    var weatherMode: WeatherMode?,
    var id: Long = -1,
    private val getShortTermForecastUseCase: GetShortTermForecastUseCase,
    private val getUltraShortTermForecastUseCase: GetUltraShortTermForecastUseCase
) : BaseViewModel() {

    private val _weatherState = MutableStateFlow<List<WeatherInfo>>(emptyList())
    val weatherState = _weatherState.asStateFlow()

    private var _weatherLiveData = MutableLiveData<ToDoDetailState>(ToDoDetailState.UnInitialized)
    val weatherLiveData: LiveData<ToDoDetailState> = _weatherLiveData

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

    fun getShortTermForecast() {
        viewModelScope.launch {
//            getShortTermForecastUseCase.invoke("20231005", "0500", 60, 127)
//                .onSuccess { predictions ->
//                    // 검색 성공 시, 결과를 StateFlow에 업데이트
//                    _weatherState.value = predictions
//                }
//                .onFailure { exception ->
//                    // 에러 처리
//                    _weatherState.value = emptyList()
//                }
        }
    }

    fun getUltraShortTermForecast() {
        viewModelScope.launch {
//            getUltraShortTermForecastUseCase.invoke("20231005", "0500", 60, 127)
//                .onSuccess { predictions ->
//                    // 검색 성공 시, 결과를 StateFlow에 업데이트
//                    _weatherState.value = predictions
//                }
//                .onFailure { exception ->
//                    // 에러 처리
//                    _weatherState.value = emptyList()
//                }
        }
    }

    fun onOpenActivityClick() {
        viewModelScope.launch {
            _navigationEvent.emit(Unit) // Activity에 신호 보내기
        }
    }
}
