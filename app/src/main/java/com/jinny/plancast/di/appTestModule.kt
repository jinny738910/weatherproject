package com.jinny.plancast.di

import android.content.Context
import androidx.room.Room
import com.jinny.plancast.data.local.db.ToDoDatabase
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.data.repository.DefaultToDoRepository
import com.jinny.plancast.data.repository.PlaceRepositoryImpl
import com.jinny.plancast.domain.repository.PlaceRepository
import com.jinny.plancast.domain.todoUseCase.*
import com.jinny.plancast.presentation.login.LoginViewModel
import com.jinny.plancast.presentation.payment.PaymentViewModel
import com.jinny.plancast.presentation.weather.WeatherMode
import com.jinny.plancast.presentation.weather.WeatherViewModel
import com.jinny.plancast.presentation.todo.detail.DetailMode
import com.jinny.plancast.presentation.todo.detail.DetailViewModel
import com.jinny.plancast.presentation.todo.list.ListViewModel
import com.jinny.plancast.presentation.weather.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // 데이터베이스 관련 의존성을 먼저 정의
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }

    // Repository 정의
    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }
    single<PlaceRepository> { PlaceRepositoryImpl(get())}

    // UseCase 정의 (Repository에 의존)
    factory { GetToDoListUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { InsertToDoUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }

    // ViewModel 정의 (UseCase에 의존)
    viewModel { ListViewModel(get(), get(), get()) }
//    viewModel { ListViewModel() }
    viewModel { LoginViewModel() }
    viewModel { PaymentViewModel() }
    viewModel { (detailMode: DetailMode, id: Long) -> DetailViewModel(detailMode, id, get(), get(), get(), get()) }
//    viewModel { (weatherMode: WeatherMode?, id: Long) -> WeatherViewModel(weatherMode, id, get(), get()) }
    viewModel { (weatherMode: WeatherMode?, id: Long) -> WeatherViewModel(weatherMode, id) }
//    viewModel { (weatherMode: WeatherMode?, id: Long) -> SearchViewModel(weatherMode, id, get()) }
    viewModel { (weatherMode: WeatherMode?, id: Long) -> SearchViewModel(weatherMode, id, get()) }
}

fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME)
        .fallbackToDestructiveMigration()
        .build()

fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()
