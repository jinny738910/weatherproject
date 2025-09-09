package aop.fastcampus.part5.chapter01.domain.weatherUseCase

import aop.fastcampus.part5.chapter01.domain.repository.WeatherRepository

/**
 * 초단기 예보 정보를 가져오는 Usecase
 * @param weatherRepository WeatherRepository 의존성 주입
 */
class GetUltraShortTermForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ) = weatherRepository.getUltraShortTermForecast(baseDate, baseTime, nx, ny)
}