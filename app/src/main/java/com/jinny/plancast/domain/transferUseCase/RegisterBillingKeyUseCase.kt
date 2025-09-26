package com.jinny.plancast.domain.transferUseCase


import com.jinny.plancast.domain.repository.TransferRepository

// 빌링키 등록이라는 단일 책임을 갖는 클래스입니다.
open class RegisterBillingKeyUseCase(
    private val transferRepository: TransferRepository // 구현체가 아닌 인터페이스에 의존
) {
    suspend operator fun invoke(authKey: String, customerKey: String): Result<Unit> {
        return transferRepository.registerBillingKey(authKey, customerKey)
    }
}
