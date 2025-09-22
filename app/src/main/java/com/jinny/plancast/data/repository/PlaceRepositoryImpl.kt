package com.jinny.plancast.data.repository

import android.content.Context
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.jinny.plancast.domain.model.WeatherInfo
import com.jinny.plancast.domain.repository.WeatherRepository
import com.jinny.plancast.data.api.WeatherApiService
import com.jinny.plancast.data.model.toDomain
import com.jinny.plancast.domain.model.PlacePrediction
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

    override suspend fun searchPlaces(query: String): Result<List<PlacePrediction>> {
        return try {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()

            val response = placesClient.findAutocompletePredictions(request).await()

            // ✅ SDK 결과를 우리 데이터 클래스로 변환
            val predictions = response.autocompletePredictions.map { prediction ->
                PlacePrediction(
                    placeId = prediction.placeId,
                    primaryText = prediction.getPrimaryText(null).toString(),
                    secondaryText = prediction.getSecondaryText(null).toString()
                )
            }
            Log.d("PlaceRepositoryImpl", "검색 성공: ${predictions}")
            Result.success(predictions)

        } catch (e: Exception) {
            Log.d("PlaceRepositoryImpl", "오류: ${e.message}")
            Result.failure(e)
        }
    }

}