package com.jinny.plancast.data.repository

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.jinny.plancast.domain.model.WeatherInfo
import com.jinny.plancast.domain.repository.WeatherRepository
import com.jinny.plancast.data.api.WeatherApiService
import com.jinny.plancast.data.model.toDomain
import com.jinny.plancast.domain.repository.PlaceRepository
import kotlinx.coroutines.tasks.await

/**
 * WeatherRepository의 구현체
 * @param apiService Retrofit으로 생성된 API 서비스
 * @param apiKey 기상청 API 인증키
 */
class PlaceRepositoryImpl (context: Context
) : PlaceRepository {

    // Places SDK의 메인 클라이언트
    private val placesClient = Places.createClient(context)

    // 장소 검색 자동완성 기능을 구현하는 함수 (코루틴 사용)


    override suspend fun searchPlaces(query: String): Result<List<AutocompletePrediction>> {
        return try {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()

            // placesClient를 호출하고, 콜백 대신 await()으로 결과 대기
            val response = placesClient.findAutocompletePredictions(request).await()
            Result.success(response.autocompletePredictions)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}