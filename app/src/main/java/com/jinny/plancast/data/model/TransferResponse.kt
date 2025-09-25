package com.jinny.plancast.data.model


/**
 *
 * 송금 API 통신에 필요한 데이터 모델
 */
data class TransferRequest(val recipient: String, val amount: Int)
data class TransferResponse(val status: String, val message: String)
