package com.jinny.plancast.domain.repository

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.jinny.plancast.domain.model.PlacePrediction
import com.jinny.plancast.domain.model.WeatherInfo

/**
 * Google pay 정보 Repository 인터페이스
 */
interface PlaceRepository {

    /**
     * 단기 예보 정보를 가져온다. (오늘, 내일, 모레 날씨)
     * @param baseDate 발표일자 (YYYYMMDD 형식)
     * @param baseTime 발표시각 (HHMM 형식)
     * @param nx X 좌표
     * @param ny Y 좌표
     * @return Result<List<WeatherInfo>> 날씨 정보 리스트 또는 에러
     */
    suspend fun searchPlaces(query: String): Result<List<PlacePrediction>>

}