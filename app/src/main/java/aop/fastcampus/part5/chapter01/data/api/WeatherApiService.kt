package aop.fastcampus.part5.chapter01.data.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import aop.fastcampus.part5.chapter01.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 기상청 API 통신을 위한 Retrofit 인터페이스
 */
interface WeatherApiService {

    // 단기예보조회
    @GET("getVilageFcst")
    suspend fun getShortTermForecast(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 1000,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): WeatherResponse

    // 초단기예보조회
    @GET("getUltraSrtFcst")
    suspend fun getUltraShortTermForecast(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 60,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): WeatherResponse
}
