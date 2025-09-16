package com.jinny.plancast.domain.todoUseCase

import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.UseCase

class DeleteToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(id: Long) {
        return toDoRepository.deleteToDoItem(id)
    }

}
