package com.jinny.plancast.data.repository

import com.jinny.plancast.data.api.TransferApiService
import com.jinny.plancast.data.model.TransferRequest
import com.jinny.plancast.data.model.TransferResponse
import com.jinny.plancast.domain.repository.TransferRepository

/**
 * TransferRepository의 구현체
 * @param apiService Retrofit으로 생성된 API 서비스
 * @param apiKey transfer API 인증키
 */
class TransferRepositoryImpl(
    private val apiService: TransferApiService
) : TransferRepository {
    override suspend fun transferMoney(recipient: String, amount: Int): TransferResponse {
        val request = TransferRequest(recipient, amount)
        return apiService.requestTransfer(request)
    }
}