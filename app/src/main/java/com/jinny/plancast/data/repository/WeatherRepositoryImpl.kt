package com.jinny.plancast.data.repository

import com.jinny.plancast.domain.model.WeatherInfo
import com.jinny.plancast.domain.repository.WeatherRepository
import com.jinny.plancast.data.api.WeatherApiService
import com.jinny.plancast.data.model.toDomain

/**
 * WeatherRepository의 구현체
 * @param apiService Retrofit으로 생성된 API 서비스
 * @param apiKey 기상청 API 인증키
 */
class WeatherRepositoryImpl (
    private val apiService: WeatherApiService,
    private val apiKey: String // BuildConfig 등 안전한 곳에 보관하는 것을 권장
) : WeatherRepository {

    override suspend fun getShortTermForecast(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ): Result<List<WeatherInfo>> {
        return try {
            val response = apiService.getShortTermForecast(
                serviceKey = apiKey,
                baseDate = baseDate,
                baseTime = baseTime,
                nx = nx,
                ny = ny
            )
            // API 응답 코드 확인 (성공 시 "00")
            if (response.response.header.resultCode == "00") {
                Result.success(response.response.body.items.item.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.response.header.resultMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUltraShortTermForecast(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ): Result<List<WeatherInfo>> {
        return try {
            val response = apiService.getUltraShortTermForecast(
                serviceKey = apiKey,
                baseDate = baseDate,
                baseTime = baseTime,
                nx = nx,
                ny = ny
            )
            if (response.response.header.resultCode == "00") {
                Result.success(response.response.body.items.item.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.response.header.resultMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}