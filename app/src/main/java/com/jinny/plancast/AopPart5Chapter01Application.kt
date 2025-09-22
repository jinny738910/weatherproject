package com.jinny.plancast

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.jinny.plancast.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import com.jinny.plancast.BuildConfig
import com.jinny.plancast.data.api.networkModule

class AopPart5Chapter01Application: Application() {

    override fun onCreate() {
        super.onCreate()

        // 1. Manifest에서 API 키를 가져옵니다.
        //    (build.gradle에 `buildConfigField` 설정이 필요할 수 있습니다)
        val apiKey = BuildConfig.MAPS_API_KEY

        // 2. Places SDK를 초기화합니다.
        //    이 코드는 앱 전체에서 딱 한 번만 실행되어야 합니다.
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AopPart5Chapter01Application)
            modules(appModule, networkModule)

        }

    }
}
