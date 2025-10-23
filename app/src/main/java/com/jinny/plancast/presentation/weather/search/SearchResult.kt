package com.jinny.plancast.presentation.weather.search

import com.google.android.gms.maps.model.LatLng

// API 응답을 표현하기 위한 간단한 데이터 클래스
data class SearchResult(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String,
    val location: LatLng? = null
)
