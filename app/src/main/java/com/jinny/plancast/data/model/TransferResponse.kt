package com.jinny.plancast.data.model

import com.jinny.plancast.domain.model.TransferResult


/**
 *
 * 송금 API 통신에 필요한 데이터 모델
 */
/**
 * 네트워크 API 응답을 위한 데이터 클래스 (DTO)
 */
data class TransferResponse(
    val transactionId: String,
    val status: String,
    val message: String
)

/**
 * Response 객체를 Domain 계층의 모델(Entity)로 변환하는 확장 함수
 */
fun TransferResponse.toDomain(): TransferResult {
    return TransferResult(
        transactionId = this.transactionId,
        status = this.status,
        message = this.message
    )
}