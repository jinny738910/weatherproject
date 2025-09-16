package aop.fastcampus.part5.chapter01.di

import aop.fastcampus.part5.chapter01.data.repository.TestToDoRepository
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.todoUseCase.DeleteAllToDoItemUseCase
import com.jinny.plancast.domain.todoUseCase.DeleteToDoItemUseCase
import com.jinny.plancast.domain.todoUseCase.GetToDoItemUseCase
import com.jinny.plancast.domain.todoUseCase.GetToDoListUseCase
import com.jinny.plancast.domain.todoUseCase.InsertToDoListUseCase
import com.jinny.plancast.domain.todoUseCase.InsertToDoUseCase
import com.jinny.plancast.domain.todoUseCase.UpdateToDoUseCase
import com.jinny.plancast.presentation.todo.detail.DetailMode
import com.jinny.plancast.presentation.todo.detail.DetailViewModel
import com.jinny.plancast.presentation.todo.list.ListViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

internal val appTestModule = module {

    factory { GetToDoListUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { InsertToDoUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }

    single<ToDoRepository> { TestToDoRepository() }

    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) -> DetailViewModel(detailMode, id, get(), get(), get(), get()) }

}
