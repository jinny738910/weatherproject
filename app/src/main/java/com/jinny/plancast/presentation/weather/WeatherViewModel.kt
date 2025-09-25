package com.jinny.plancast.presentation.weather

import android.annotation.SuppressLint
import android.util.Log
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.ZoneId


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

    @SuppressLint("NewApi")
    fun getBaseDate(): String {
        // 1. 현재 날짜를 가져옵니다.
        val currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"))

        // 2. 날짜를 "yyyyMMdd" 형식으로 포맷합니다.
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val baseDate = currentDate.format(formatter)

        Log.d("weatherViewModel", "기상 데이터 요청 날짜 :${baseDate}")
        return baseDate
    }

    @SuppressLint("NewApi")
    private fun getSafeBaseTime(): String {
        val currentTime = LocalTime.now(ZoneId.of("Asia/Seoul"))

        // 현재 분이 40분보다 작으면 이전 시간대를 사용합니다.
        // (예: 15시 20분이라면 14시 데이터를 요청)
        val targetTime = if (currentTime.minute < 40) {
            currentTime.minusHours(1)
        } else {
            currentTime
        }

        Log.d("weatherViewModel", "기상 데이터 요청 시간 :${targetTime}")
        // 시간을 "HH", 분을 "00"으로 고정하여 포맷합니다.
        return targetTime.format(DateTimeFormatter.ofPattern("HH00"))
    }

    fun getShortTermForecast() {
        val baseDate = getBaseDate()
        val baseTime = getSafeBaseTime()

        viewModelScope.launch {
//            getShortTermForecastUseCase.invoke(baseDate, baseTime, 55, 127)
            getShortTermForecastUseCase.invoke("0925", "0500", 55, 127)
                .onSuccess { predictions ->
                    predictions?.forEachIndexed { index, weatherInfo ->
                        // 반복문으로 리스트의 각 요소를 하나씩 로그에 찍습니다.
                        Log.d("weatherViewModel", "검색 성공: Item at index $index: $weatherInfo")
                    }

                    // 검색 성공 시, 결과를 StateFlow에 업데이트
                    _weatherState.value = predictions
                }
                .onFailure { exception ->
                    Log.d("weatherViewModel", "에러 처리 !! : ${exception.message}")
                    // 에러 처리
                    _weatherState.value = emptyList()
                }
        }
    }

    fun getUltraShortTermForecast() {
        viewModelScope.launch {
            getUltraShortTermForecastUseCase.invoke("20250925", "0500", 55, 127)
                .onSuccess { predictions ->
                    // 검색 성공 시, 결과를 StateFlow에 업데이트
                    _weatherState.value = predictions
                }
                .onFailure { exception ->
                    // 에러 처리
                    _weatherState.value = emptyList()
                }
        }
    }

    fun onOpenActivityClick() {
        viewModelScope.launch {
            _navigationEvent.emit(Unit) // Activity에 신호 보내기
        }
    }
}
