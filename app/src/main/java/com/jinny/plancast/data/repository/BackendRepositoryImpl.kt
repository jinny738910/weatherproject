package com.jinny.plancast.data.repository

import android.content.Context
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.jinny.plancast.data.api.BackendApiService
import com.jinny.plancast.domain.model.PlacePrediction
import com.jinny.plancast.domain.repository.BackendRepository
import com.jinny.plancast.domain.repository.PlaceRepository
import kotlinx.coroutines.tasks.await
import java.io.IOException

/**
 * BackendRepository의 구현체
 * @param apiService Retrofit으로 생성된 API 서비스
 * @param apiKey 기상청 API 인증키
 */
class BackendRepositoryImpl (
    private val apiService: BackendApiService
) : BackendRepository {

    override suspend fun fetchHelloMessage(): Result<String> {
        return try {
            val message = apiService.getHelloMessage()
            Log.d("BackendRepositoryImpl", "backend fetchHelloMessage  :${message}")
            // 성공 시 Result.success() 반환
            Result.success(message)
        } catch (e: Exception) {
            Log.d("BackendRepositoryImpl", "backend fetchHelloMessage error:${e.message}")
            // 네트워크 오류, 서버 응답 오류 등 예외 처리
            when (e) {
                is IOException -> Result.failure(Exception("네트워크 연결 실패."))
                else -> Result.failure(Exception("API 호출 실패: ${e.message}"))
            }
        }
    }
}