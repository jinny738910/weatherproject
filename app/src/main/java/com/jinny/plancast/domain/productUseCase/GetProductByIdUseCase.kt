package com.jinny.plancast.domain.productUseCase

import android.util.Log
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.domain.repository.ProductRepository

open class GetProductsByIdUseCase (
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Long): Result<Product> {
        return try {
            val products = repository.getProductById(id)
            Result.success(products)
        } catch (e: Exception) {
            Log.d ("GetProductsByIdUseCase", "Error creating product: ${e.message}")
            Result.failure(e)
        }
    }
}