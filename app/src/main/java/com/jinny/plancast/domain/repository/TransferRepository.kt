package com.jinny.plancast.domain.repository

import com.jinny.plancast.data.model.TransferResponse


/**
 *  송금 관련 Repository 인터페이스
 */


interface TransferRepository {
    suspend fun transferMoney(recipient: String, amount: Int): TransferResponse
}