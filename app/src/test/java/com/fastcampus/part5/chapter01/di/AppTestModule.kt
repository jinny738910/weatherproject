package com.fastcampus.part5.chapter01.di

import com.jinny.plancast.data.repository.ToDoRepository
import com.fastcampus.part5.chapter01.data.repository.TestToDoRepository
import com.jinny.plancast.domain.todoUseCase.*
import com.jinny.plancast.presentation.todo.detail.DetailMode
import com.jinny.plancast.presentation.todo.list.ListViewModel
import com.jinny.plancast.presentation.todo.detail.DetailViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appTestModule = module {

    factory { com.jinny.plancast.domain.usecase.todoUseCase.GetToDoListUseCase(get()) }
    factory { com.jinny.plancast.domain.usecase.todoUseCase.GetToDoItemUseCase(get()) }
    factory { com.jinny.plancast.domain.usecase.todoUseCase.InsertToDoListUseCase(get()) }
    factory { com.jinny.plancast.domain.usecase.todoUseCase.InsertToDoUseCase(get()) }
    factory { com.jinny.plancast.domain.usecase.todoUseCase.DeleteToDoItemUseCase(get()) }
    factory { com.jinny.plancast.domain.usecase.todoUseCase.DeleteAllToDoItemUseCase(get()) }
    factory { com.jinny.plancast.domain.usecase.todoUseCase.UpdateToDoUseCase(get()) }

    single<ToDoRepository> { TestToDoRepository() }

    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) -> DetailViewModel(detailMode, id, get(), get(), get(), get()) }

}
