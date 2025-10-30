package com.jinny.plancast.data.repository

import android.util.Log
import com.jinny.plancast.data.api.BackendApiService
import com.jinny.plancast.data.api.ProductApiService
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.domain.repository.BackendRepository
import com.jinny.plancast.domain.repository.ProductRepository
import java.io.IOException

/**
 * ProductRepository의 구현체
 * @param apiService Retrofit으로 생성된 API 서비스
 * @param apiKey 기상청 API 인증키
 */
class ProductRepositoryImpl (
    private val productapiService: ProductApiService
) : ProductRepository {

    // R: 모든 상품 조회 구현
    override suspend fun getProducts(): List<Product> {
        return productapiService.getAllProducts()
    }

    // C: 상품 생성 구현
    override suspend fun createProduct(product: Product): Product {
        return productapiService.createProduct(product)
    }

    // ... 나머지 CRUD 메서드도 ProductApi를 호출하여 구현

    override suspend fun getProductById(id: Long): Product {
        return productapiService.getProductById(id)
    }

    override suspend fun updateProduct(id: Long, product: Product): Product {
        // 백엔드 컨트롤러에 맞게 ID를 Path로 넘기고 Body에 업데이트 내용을 포함
        return productapiService.updateProduct(id, product)
    }

    override suspend fun deleteProduct(id: Long) {
        productapiService.deleteProduct(id)
    }
}