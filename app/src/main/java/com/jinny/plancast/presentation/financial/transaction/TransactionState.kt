package com.jinny.plancast.presentation.financial.transaction

import com.jinny.plancast.data.model.Product


sealed class TransactionState {
    object Loading : TransactionState()
    data class Success(val products: List<Product>) : TransactionState()
    data class Error(val message: String) : TransactionState()
    object Idle : TransactionState() // 초기 상태
}






