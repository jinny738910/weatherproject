package com.jinny.plancast.domain.usecase.productUseCase

import android.util.Log
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.domain.repository.ProductRepository
import javax.inject.Inject

open class CreateProductsUseCase @Inject constructor(
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