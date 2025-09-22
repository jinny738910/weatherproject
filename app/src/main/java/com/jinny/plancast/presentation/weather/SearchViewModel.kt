package com.jinny.plancast.presentation.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.jinny.plancast.domain.model.PlacePrediction
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
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

//    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
//    val searchResults = _searchResults.asStateFlow()

    private val _searchResults = MutableStateFlow<List<PlacePrediction>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private var _weatherLiveData = MutableLiveData<WeatherState>(WeatherState.UnInitialized)
    val weatherLiveData: LiveData<WeatherState> = _weatherLiveData


    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length > 1) {
            viewModelScope.launch {
                placeRepository.searchPlaces(query)
                    .onSuccess { predictions ->
                        // 검색 성공 시, 결과를 StateFlow에 업데이트
                        _searchResults.value = predictions
                        Log.d("SearchViewModel", "검색 성공: ${predictions}")
                    }
                    .onFailure { exception ->
                        // 에러 처리
                        _searchResults.value = emptyList()
                    }
            }
        } else {
            _searchResults.value = emptyList()
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

}