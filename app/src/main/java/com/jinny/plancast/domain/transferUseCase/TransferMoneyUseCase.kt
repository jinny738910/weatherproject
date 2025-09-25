package com.jinny.plancast.domain.transferUseCase


import com.jinny.plancast.data.model.TransferResponse
import com.jinny.plancast.domain.repository.TransferRepository

open class TransferMoneyUseCase(
    private val transferRepository: TransferRepository
) {
    open suspend fun execute(recipient: String, amount: Int): TransferResponse {
        // 실제 송금 로직(유효성 검사 등)을 추가할 수 있습니다.
        if (amount <= 0) {
            throw IllegalArgumentException("송금 금액은 0보다 커야 합니다.")
        }
        return transferRepository.transferMoney(recipient, amount)
    }
}
