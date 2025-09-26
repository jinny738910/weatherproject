package com.jinny.plancast.domain.model

// 앱의 UI Layer나 Domain Layer에서 사용할 정제된 송금 정보 데이터 클래스

data class TransferResult(
    val transactionId: String,
    val status: String,
    val message: String
)
