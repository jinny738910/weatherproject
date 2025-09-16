package com.jinny.plancast.di

import android.content.Context
import androidx.room.Room
import com.jinny.plancast.data.local.db.ToDoDatabase
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.data.repository.DefaultToDoRepository
import com.jinny.plancast.domain.todoUseCase.*
import com.jinny.plancast.presentation.login.LoginViewModel
import com.jinny.plancast.presentation.payment.PaymentViewModel
import com.jinny.plancast.presentation.weather.WeatherMode
import com.jinny.plancast.presentation.weather.WeatherViewModel
import com.jinny.plancast.presentation.todo.detail.DetailMode
import com.jinny.plancast.presentation.todo.detail.DetailViewModel
import com.jinny.plancast.presentation.todo.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    factory { GetToDoListUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { InsertToDoUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }

    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }

    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }

//    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { LoginViewModel() }
    viewModel { ListViewModel() }
    viewModel { PaymentViewModel() }
    viewModel { (detailMode: DetailMode, id: Long) -> DetailViewModel(detailMode, id, get(), get(), get(), get()) }
    viewModel { (weatherMode: WeatherMode?, id: Long) -> WeatherViewModel(weatherMode, id) }
}

internal fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()
