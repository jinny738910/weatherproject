package com.jinny.plancast.domain.repository

import com.jinny.plancast.domain.model.WeatherInfo

/**
 * 기상청 날씨 정보 Repository 인터페이스
 */
interface WeatherRepository {

    /**
     * 단기 예보 정보를 가져온다. (오늘, 내일, 모레 날씨)
     * @param baseDate 발표일자 (YYYYMMDD 형식)
     * @param baseTime 발표시각 (HHMM 형식)
     * @param nx X 좌표
     * @param ny Y 좌표
     * @return Result<List<WeatherInfo>> 날씨 정보 리스트 또는 에러
     */
    suspend fun getShortTermForecast(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ): Result<List<WeatherInfo>>

    /**
     * 초단기 강수 예측 정보를 가져온다. (현 시간부터 1시간 간격 6시간 예측)
     * @param baseDate 발표일자 (YYYYMMDD 형식)
     * @param baseTime 발표시각 (HHMM 형식)
     * @param nx X 좌표
     * @param ny Y 좌표
     * @return Result<List<WeatherInfo>> 강수 정보 리스트 또는 에러
     */
    suspend fun getUltraShortTermForecast(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ): Result<List<WeatherInfo>>
}