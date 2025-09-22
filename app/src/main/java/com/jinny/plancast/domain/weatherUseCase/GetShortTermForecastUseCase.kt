package com.jinny.plancast.domain.weatherUseCase

import com.jinny.plancast.domain.repository.WeatherRepository


/**
 * 단기 예보 정보를 가져오는 Usecase
 * @param weatherRepository WeatherRepository 의존성 주입
 */
open class GetShortTermForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    open suspend operator fun invoke(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ) = weatherRepository.getShortTermForecast(baseDate, baseTime, nx, ny)
}