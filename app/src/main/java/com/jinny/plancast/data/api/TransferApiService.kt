package com.jinny.plancast.data.api

import com.jinny.plancast.data.model.TransferResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

/**
 * 송금 API 통신을 위한 Retrofit 인터페이스
 */
interface TransferApiService {
    @POST("/api/toss/issue-billing-key")
    suspend fun requestBillingKey(
        @Body authKeyRequest: Map<String, String>
    ): Response<Unit> // 성공 여부만 받음

    // 실제 결제(송금) 요청
    @POST("/api/toss/execute-payment")
    suspend fun executePayment(
        @Body paymentBody: Map<String, Any>
    ): Result<TransferResponse>
}