package com.jinny.plancast.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

// 네트워크 관련 의존성을 정의하는 Koin 모듈
val networkModule = module {

    // 1. Retrofit 인스턴스 생성 방법을 정의
    single {
        Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(get()) // 아래에서 정의한 OkHttpClient를 주입받음
            .build()
    }

    // Retrofit 싱글톤 객체 제공
    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 2. WeatherApiService 인스턴스 생성 방법을 정의
    single {
        // 위에서 만든 Retrofit 인스턴스를 Koin에게서 받아(get) ApiService를 생성
        get<Retrofit>().create(WeatherApiService::class.java)
    }

    // TransferApiService 싱글톤 객체 제공
    single {
        get<Retrofit>().create(TransferApiService::class.java)
    }

    // BackendApiService 싱글톤 객체 제공
    single {
        get<Retrofit>().create(BackendApiService::class.java)
    }

    // 3. OkHttpClient 인스턴스 생성 방법을 정의 (로깅 인터셉터 포함)
    single {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    // 4. HttpLoggingInterceptor 인스턴스 생성 방법을 정의
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}