package com.jinny.plancast.presentation.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.jinny.plancast.domain.repository.PlaceRepository

import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    var weatherMode: WeatherMode?,
    var id: Long = -1,
//    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

//    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
//    val searchResults = _searchResults.asStateFlow()

    private val _searchResults = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private var _weatherLiveData = MutableLiveData<WeatherState>(WeatherState.UnInitialized)
    val weatherLiveData: LiveData<WeatherState> = _weatherLiveData

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length > 1) {
            viewModelScope.launch {
//                placeRepository.searchPlaces(query)
//                    .onSuccess { predictions ->
//                        // 검색 성공 시, 결과를 StateFlow에 업데이트
//                        _searchResults.value = predictions
//                    }
//                    .onFailure { exception ->
//                        // 에러 처리
//                        _searchResults.value = emptyList()
//                    }
            }
        } else {
            _searchResults.value = emptyList()
        }
    }

    private fun searchPlaces(query: String) {
        viewModelScope.launch {

            viewModelScope.launch {
                try {
                    // 1. maps_local 도구를 사용하여 장소 검색 API 호출


                    // 2. API 응답(Place)을 UI에서 사용할 데이터 클래스(SearchResult)로 변환
//                    val results = places.map { place ->
//                        SearchResult(
//                            placeId = place.id.removePrefix("place_id://"), // "place_id://" 접두사 제거
//                            primaryText = place.display_name,
//                            secondaryText = place.formatted_address,
//                            location = place.location?.let { LatLng(it.latitude, it.longitude) }
//                        )
//                    }

                    // 3. 변환된 결과를 StateFlow에 업데이트하여 UI에 반영
//                    _searchResults.value = results

                } catch (e: Exception) {
                    // API 호출 중 에러가 발생했을 때 처리
                    // TODO: 사용자에게 에러 메시지를 보여주는 UI 상태 업데이트
                    _searchResults.value = emptyList()
                }
            }

//            // 아래는 API 호출 결과를 가정한 더미 데이터입니다.
//            val dummyResults = listOf(
//                SearchResult("place_id_1", "에펠탑", "프랑스 파리", LatLng(48.8584, 2.2945)),
//                SearchResult("place_id_2", "롯데월드타워", "대한민국 서울특별시", LatLng(37.5126, 127.1025)),
//                SearchResult("place_id_3", "도쿄 타워", "일본 도쿄도", LatLng(35.6586, 139.7454))
//            )
//            _searchResults.value = dummyResults
        }
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

    fun onOpenActivityClick() {
        viewModelScope.launch {
            _navigationEvent.emit(Unit) // Activity에 신호 보내기
        }
    }
}
