package com.jinny.plancast.di

import android.content.Context
import androidx.room.Room
import com.jinny.plancast.BuildConfig
import com.jinny.plancast.data.local.db.ToDoDatabase
import com.jinny.plancast.data.repository.BackendRepositoryImpl
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.data.repository.DefaultToDoRepository
import com.jinny.plancast.data.repository.PlaceRepositoryImpl
import com.jinny.plancast.data.repository.TransferRepositoryImpl
import com.jinny.plancast.data.repository.WeatherRepositoryImpl
import com.jinny.plancast.domain.repository.BackendRepository
import com.jinny.plancast.domain.repository.PlaceRepository
import com.jinny.plancast.domain.repository.TransferRepository
import com.jinny.plancast.domain.repository.WeatherRepository
import com.jinny.plancast.domain.todoUseCase.*
import com.jinny.plancast.domain.transferUseCase.ExecuteTransferUseCase
import com.jinny.plancast.domain.transferUseCase.RegisterBillingKeyUseCase

import com.jinny.plancast.domain.weatherUseCase.GetShortTermForecastUseCase
import com.jinny.plancast.domain.weatherUseCase.GetUltraShortTermForecastUseCase
import com.jinny.plancast.presentation.alarm.AlarmListViewModel
import com.jinny.plancast.presentation.chat.ChatListViewModel
import com.jinny.plancast.presentation.chat.ChatUserListViewModel
import com.jinny.plancast.presentation.chat.ChatRoomViewModel
import com.jinny.plancast.presentation.login.LoginViewModel
import com.jinny.plancast.presentation.financial.password.PasswordViewModel
import com.jinny.plancast.presentation.financial.payment.PaymentViewModel
import com.jinny.plancast.presentation.weather.WeatherMode
import com.jinny.plancast.presentation.weather.WeatherViewModel
import com.jinny.plancast.presentation.todo.detail.DetailMode
import com.jinny.plancast.presentation.todo.detail.DetailViewModel
import com.jinny.plancast.presentation.todo.list.ListViewModel
import com.jinny.plancast.presentation.financial.transfer.TransferViewModel
import com.jinny.plancast.presentation.setting.SettingViewModel
import com.jinny.plancast.presentation.weather.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named

val appModule = module {


    val ApiKeyQualifier = named("ApiKey")
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    single(ApiKeyQualifier) { BuildConfig.WEATHER_API_KEY }
    // 데이터베이스 관련 의존성을 먼저 정의
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }


    // Repository 정의
    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }
    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }
    single<PlaceRepository> { PlaceRepositoryImpl(get())}
    single<WeatherRepository> { WeatherRepositoryImpl(apiService = get(), apiKey = get(ApiKeyQualifier))}
    single<TransferRepository> { TransferRepositoryImpl(transferService = get())}
    single<BackendRepository> { BackendRepositoryImpl(get()) }


    // UseCase 정의 (Repository에 의존)
    factory { GetToDoListUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { InsertToDoUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { ExecuteTransferUseCase(get()) }
    factory { RegisterBillingKeyUseCase(get()) }

    factory { GetShortTermForecastUseCase(get()) }
    factory { GetUltraShortTermForecastUseCase(get()) }

    // ViewModel 정의 (UseCase에 의존)
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { LoginViewModel() }
    viewModel { (detailMode: DetailMode, id: Long) -> DetailViewModel(detailMode, id, get(), get(), get(), get()) }
    viewModel { (weatherMode: WeatherMode?, id: Long) -> WeatherViewModel(weatherMode, id, get(), get()) }
    viewModel { (weatherMode: WeatherMode?, id: Long) -> SearchViewModel(weatherMode, id, get()) }
    viewModel { (id: Long) -> PasswordViewModel(id) }
    viewModel { TransferViewModel(get(), get()) }
    viewModel { SettingViewModel() }
    viewModel { AlarmListViewModel() }
    viewModel { PaymentViewModel(get()) }
    viewModel { ChatUserListViewModel() }
    viewModel { ChatRoomViewModel() }
    viewModel { ChatListViewModel() }

}

fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME)
        .fallbackToDestructiveMigration()
        .build()

fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()
