package com.jinny.plancast.data.api

import com.jinny.plancast.data.model.TransferRequest
import com.jinny.plancast.data.model.TransferResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 송금 API 통신을 위한 Retrofit 인터페이스
 */
interface TransferApiService {
    @POST("/api/transfer")
    suspend fun requestTransfer(@Body request: TransferRequest): TransferResponse
}