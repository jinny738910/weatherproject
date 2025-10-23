package com.jinny.plancast.domain.usecase.productUseCase

import android.util.Log
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.domain.repository.ProductRepository

open class GetProductsUseCase (
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return try {
            val products = repository.getProducts()
            Result.success(products)
        } catch (e: Exception) {
            Log.d ("GetProductsUseCase", "Error getting products: ${e.message}")
            Result.failure(e)
        }
    }
}