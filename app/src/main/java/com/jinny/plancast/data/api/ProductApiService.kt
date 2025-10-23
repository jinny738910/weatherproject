package com.jinny.plancast.data.api

import com.jinny.plancast.data.model.Product
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * 송금 API 통신을 위한 Retrofit 인터페이스
 */
interface ProductApiService {
    /// R: 모든 상품 조회 (GET /api/products)
    @GET("/api/products")
    suspend fun getAllProducts(): List<Product>

    // C: 상품 생성 (POST /api/products)
    @POST("/api/products")
    suspend fun createProduct(@Body product: Product): Product

    // R: ID로 상품 조회 (GET /api/products/{id})
    @GET("/api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product

    // U: 상품 수정 (PUT /api/products/{id})
    @PUT("/api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body product: Product): Product

    // D: 상품 삭제 (DELETE /api/products/{id})
    @DELETE("/api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long)
}