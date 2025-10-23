package aop.fastcampus.part5.chapter01.di

import aop.fastcampus.part5.chapter01.data.repository.TestToDoRepository
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.usecase.todoUseCase.DeleteAllToDoItemUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.DeleteToDoItemUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.GetToDoItemUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.GetToDoListUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.InsertToDoListUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.InsertToDoUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.UpdateToDoUseCase
import com.jinny.plancast.presentation.todo.detail.DetailMode
import com.jinny.plancast.presentation.todo.detail.DetailViewModel
import com.jinny.plancast.presentation.todo.list.ListViewModel
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
