package com.jinny.plancast.domain.productUseCase

import android.util.Log
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.domain.repository.ProductRepository

open class CreateProductsUseCase (
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Product> {
        return try {
            val products = repository.createProduct(product)
            Result.success(products)
        } catch (e: Exception) {
            Log.d ("CreateProductsUseCase", "Error creating product: ${e.message}")
            Result.failure(e)
        }
    }
}