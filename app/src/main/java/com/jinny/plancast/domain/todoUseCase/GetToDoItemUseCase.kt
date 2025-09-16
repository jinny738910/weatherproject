package com.jinny.plancast.domain.todoUseCase

import com.jinny.plancast.data.entity.ToDoEntity
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.UseCase

class GetToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(id: Long): ToDoEntity? {
        return toDoRepository.getToDoItem(id)
    }

}
