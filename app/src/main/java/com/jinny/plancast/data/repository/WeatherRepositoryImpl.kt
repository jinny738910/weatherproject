package com.jinny.plancast.data.repository

import android.util.Log
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
            Log.d("WeatherRepositoryImpl", "getShortTermForecast 성공 !! 메세지:${response.response.header.resultCode},  ${response.response.header.resultMsg}")
            // API 응답 코드 확인 (성공 시 "00")
            if (response.response.header.resultCode == "00") {
                Log.d("WeatherRepositoryImpl", "result code 00 !! body dataType:${response.response.body.dataType}")
                Log.d("WeatherRepositoryImpl", "result code 00 !! body items:${response.response.body.items.item.size}")
                Log.d("WeatherRepositoryImpl", "result code 00 !! body pageNo:${response.response.body.pageNo}")
                Log.d("WeatherRepositoryImpl", "result code 00 !! body numOfRows:${response.response.body.numOfRows}")
                Log.d("WeatherRepositoryImpl", "result code 00 !! body totalCount:${response.response.body.totalCount}")
                Log.d("WeatherRepositoryImpl", "result code 00 !! body items:${response.response.body.items} ")

                val itemsList = response.response?.body?.items?.item
                itemsList?.forEachIndexed { index, weatherInfo ->
                    // 반복문으로 리스트의 각 요소를 하나씩 로그에 찍습니다.
                    Log.d("WeatherRepositoryImpl", "Item at index $index: $weatherInfo")
                }
                Result.success(response.response.body.items.item.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.response.header.resultMsg))
            }
        } catch (e: Exception) {
            Log.d("WeatherRepositoryImpl", "getShortTermForecast 에러 메세지: ${e.message}")
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
            Log.d("WeatherRepositoryImpl", "getUltraShortTermForecast 메세지: ${response.response.header.resultMsg}")
            if (response.response.header.resultCode == "00") {
                Result.success(response.response.body.items.item.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.response.header.resultMsg))
            }
        } catch (e: Exception) {
            Log.d("WeatherRepositoryImpl", "getShortTermForecast 에러 메세지: ${e.message}")
            Result.failure(e)
        }
    }
}