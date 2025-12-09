package com.jinny.plancast.di

import com.google.gson.GsonBuilder
import com.jinny.plancast.data.api.BackendApiService
import com.jinny.plancast.data.api.ProductApiService
import com.jinny.plancast.data.api.TransferApiService
import com.jinny.plancast.data.api.WeatherApiService
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
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherNetwork // 기상청용 이름표

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackendNetwork // 내 백엔드용 이름표

@Module
@InstallIn(SingletonComponent::class) // 앱이 켜져있는 동안 싱글톤 유지
object NetworkModule {

    // --- 1. 공통 인프라 (OkHttp, Logger) ---

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // --- 2. Retrofit 인스턴스 (두 가지 버전) ---

    // A. 기상청용 Retrofit (WeatherNetwork 라벨 부착)
    @Provides
    @Singleton
    @WeatherNetwork
    fun provideWeatherRetrofit(
        okHttpClient: OkHttpClient // 위에서 만든 클라이언트 주입
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()
    }

    // B. 백엔드용 Retrofit (BackendNetwork 라벨 부착)
    @Provides
    @Singleton
    @BackendNetwork
    fun provideBackendRetrofit(
        okHttpClient: OkHttpClient // 백엔드 통신에도 타임아웃/로깅 적용 권장 (Koin코드엔 없었지만 추가함)
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // 보통 여기도 클라이언트를 공유해서 씁니다.
            .build()
    }

    // --- 3. API Services (각자에 맞는 Retrofit 선택) ---

    // WeatherApiService는 -> @WeatherNetwork Retrofit 사용
    @Provides
    @Singleton
    fun provideWeatherApiService(
        @WeatherNetwork retrofit: Retrofit
    ): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    // 나머지 서비스들은 -> @BackendNetwork Retrofit 사용

    @Provides
    @Singleton
    fun provideTransferApiService(
        @BackendNetwork retrofit: Retrofit
    ): TransferApiService {
        return retrofit.create(TransferApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBackendApiService(
        @BackendNetwork retrofit: Retrofit
    ): BackendApiService {
        return retrofit.create(BackendApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductApiService(
        @BackendNetwork retrofit: Retrofit
    ): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }
}