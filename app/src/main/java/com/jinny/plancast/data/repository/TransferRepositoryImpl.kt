package com.jinny.plancast.data.repository

import com.jinny.plancast.data.api.TransferApiService
import com.jinny.plancast.data.model.toDomain
import com.jinny.plancast.domain.model.TransferResult
import com.jinny.plancast.domain.repository.TransferRepository

/**
 * TransferRepository의 구현체
 * @param apiService Retrofit으로 생성된 API 서비스
 * @param apiKey transfer API 인증키
 */
class TransferRepositoryImpl(
    private val transferService: TransferApiService
) : TransferRepository {

    override suspend fun registerBillingKey(authKey: String, customerKey: String): Result<Unit> {
        return try {
            val response = transferService.requestBillingKey(mapOf("authKey" to authKey, "customerKey" to customerKey))

            if (response.isSuccessful) {
                // HTTP 통신 성공
                Result.success(Unit)
            } else {
                // HTTP 통신은 성공했으나, 서버에서 에러 응답 (4xx, 5xx)
                val errorMsg = "빌링키 발급에 실패했습니다. (코드: ${response.code()})"
                Result.failure(Exception(errorMsg))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun executeTransfer(amount: Long, orderName: String, customerKey: String): Result<TransferResult> {
        return try {
            val paymentBody = mapOf("amount" to amount, "orderName" to orderName, "customerKey" to customerKey)
            val response = transferService.executePayment(paymentBody)

            Result.success(response.getOrThrow().toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}