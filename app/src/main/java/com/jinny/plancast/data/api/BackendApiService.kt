package com.jinny.plancast.data.api

import com.jinny.plancast.data.model.TransferResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET

/**
 * 결제 API 통신을 위한 Retrofit 인터페이스
 */
interface BackendApiService {
    // 백엔드의 @GetMapping("/hello")와 매핑됩니다.
    // suspend 키워드를 사용하여 코루틴에서 비동기 호출이 가능합니다.
    @GET("hello")
    suspend fun getHelloMessage(): String // String 타입으로 응답을 받습니다.
}