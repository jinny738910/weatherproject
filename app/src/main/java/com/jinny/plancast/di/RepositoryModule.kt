package com.jinny.plancast.di

import com.jinny.plancast.data.repository.BackendRepositoryImpl
import com.jinny.plancast.data.repository.DefaultToDoRepository
import com.jinny.plancast.data.repository.PlaceRepositoryImpl
import com.jinny.plancast.data.repository.ProductRepositoryImpl
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.data.repository.TransferRepositoryImpl
import com.jinny.plancast.data.repository.WeatherRepositoryImpl
import com.jinny.plancast.domain.repository.BackendRepository
import com.jinny.plancast.domain.repository.PlaceRepository
import com.jinny.plancast.domain.repository.ProductRepository
import com.jinny.plancast.domain.repository.TransferRepository
import com.jinny.plancast.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // 앱이 켜져있는 동안 싱글톤 유지
abstract class RepositoryModule {
    // 1. ToDoRepository (이전에 하신 내용)
    @Binds
    @Singleton
    abstract fun bindToDoRepository(
        impl: DefaultToDoRepository
    ): ToDoRepository

    // 2. PlaceRepository
    @Binds
    @Singleton
    abstract fun bindPlaceRepository(
        impl: PlaceRepositoryImpl
    ): PlaceRepository

    // 3. WeatherRepository
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    // 4. TransferRepository
    @Binds
    @Singleton
    abstract fun bindTransferRepository(
        impl: TransferRepositoryImpl
    ): TransferRepository

    // 5. BackendRepository
    @Binds
    @Singleton
    abstract fun bindBackendRepository(
        impl: BackendRepositoryImpl
    ): BackendRepository

    // 6. ProductRepository
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
}