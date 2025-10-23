package com.jinny.plancast.data.model

data class Product(
    val id: Long? = null,
    val name: String,
    val price: Int,
    val selectedPaymentMethod: String,
    val availablePaymentMethods: List<String> = listOf()
)