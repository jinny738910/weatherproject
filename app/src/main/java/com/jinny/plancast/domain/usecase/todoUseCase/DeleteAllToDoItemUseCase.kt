package com.jinny.plancast.domain.usecase.todoUseCase

import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.usecase.UseCase

class DeleteAllToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke() {
        return toDoRepository.deleteAll()
    }

}
