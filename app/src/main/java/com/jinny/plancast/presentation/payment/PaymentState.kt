package com.jinny.plancast.presentation.payment

import com.google.firebase.auth.FirebaseUser


// 1. UI의 모든 정보를 담는 상태(State) 데이터 클래스
data class PaymentUiState(
    val itemName: String = "PlanCast 프리미엄 1년 구독",
    val price: Int = 49900,
    val selectedPaymentMethod: String = "KB국민카드 **** 1234",
    val availablePaymentMethods: List<String> = listOf(
        "KB국민카드 **** 1234",
        "신한카드 **** 5678",
        "카카오페이",
        "네이버페이"
    )
)




