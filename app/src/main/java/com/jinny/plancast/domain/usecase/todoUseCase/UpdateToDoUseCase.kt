package com.jinny.plancast.domain.usecase.todoUseCase

import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.usecase.UseCase

class UpdateToDoUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(toDoEntity: ToDoEntity) {
        return toDoRepository.updateToDoItem(toDoEntity)
    }

}
