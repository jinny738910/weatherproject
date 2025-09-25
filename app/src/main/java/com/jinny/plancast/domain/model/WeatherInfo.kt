package com.jinny.plancast.domain.model

/**
 * 앱의 UI Layer나 Domain Layer에서 사용할 정제된 날씨 정보 데이터 클래스
 *
 * @property category 예보 항목 (기온, 습도, 하늘상태 등)
 * @property forecastDate 예보 날짜 (YYYYMMDD)
 * @property forecastTime 예보 시간 (HHMM)
 * @property forecastValue 예보 값
 */
data class WeatherInfo(
    val category: WeatherCategory,
    val forecastDate: String,
    val forecastTime: String,
    val forecastValue: Double

)