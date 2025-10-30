package com.jinny.plancast.domain.model

/**
 * 앱의 UI Layer나 Domain Layer에서 사용할 정제된 위치 정보 데이터 클래스
 *
 * @property placeId
 * @property primaryText
 * @property secondaryText
 */
data class PlacePrediction(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String
)