package com.jinny.plancast.domain.repository

import com.jinny.plancast.domain.model.TransferResult


/**
 *  송금 관련 Repository 인터페이스
 */


interface TransferRepository {
    /**
     * 우리 백엔드 서버에 authKey를 보내 빌링키 발급을 요청합니다.
     */
    suspend fun registerBillingKey(authKey: String, customerKey: String): Result<Unit>

    /**
     * 우리 백엔드 서버에 결제를 요청합니다.
     */
    suspend fun executeTransfer(amount: Long, orderName: String, customerKey: String): Result<TransferResult>
}