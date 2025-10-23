package com.jinny.plancast.domain.usecase.productUseCase

import android.util.Log
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.domain.repository.ProductRepository

open class DeleteProductsUseCase (
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Long) {
        return try {
            repository.deleteProduct(id)
        } catch (e: Exception) {
            Log.d ("DeleteProductsUseCase", "Error creating product: ${e.message}")
            throw e
        }
    }
}