package com.jinny.plancast.di

import android.content.Context
import androidx.room.Room
import com.jinny.plancast.BuildConfig
import com.jinny.plancast.data.local.db.ToDoDatabase
import com.jinny.plancast.data.local.db.dao.ToDoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherIO

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherMain

@Module
@InstallIn(SingletonComponent::class) // 앱 살아있는 동안 유지 (== single)
object AppModule {

    // --- 1. Qualifiers & Dispatchers ---

    @Provides
    @Singleton
    @Named("ApiKey") // Koin의 named("ApiKey") 대체
    fun provideApiKey(): String {
        return BuildConfig.WEATHER_API_KEY
    }

    // 1. 라벨(Annotation) 만들기


    @Provides
    @DispatcherIO // 이 라벨이 붙은 곳에는 IO Dispatcher를 줌
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DispatcherMain
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main


    // --- 2. Database (Room) ---

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): ToDoDatabase {
        return Room.databaseBuilder(
            context,
            ToDoDatabase::class.java,
            ToDoDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideToDoDao(database: ToDoDatabase): ToDoDao {
        return database.toDoDao()
    }

}

