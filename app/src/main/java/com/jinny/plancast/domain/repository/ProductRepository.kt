package com.jinny.plancast.domain.repository

import com.jinny.plancast.data.model.Product

/**
 * 알림 스케줄링 관련 Repository 인터페이스
 */
interface ProductRepository {

    suspend fun getProducts(): List<Product>
    suspend fun getProductById(id: Long): Product
    suspend fun createProduct(product: Product): Product
    suspend fun updateProduct(id: Long, product: Product): Product
    suspend fun deleteProduct(id: Long)
}