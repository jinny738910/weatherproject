package com.fastcampus.part5.chapter01.domain.todoUseCase

import com.fastcampus.part5.chapter01.data.repository.ToDoRepository
import com.fastcampus.part5.chapter01.domain.UseCase

internal class DeleteToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(id: Long) {
        return toDoRepository.deleteToDoItem(id)
    }

}
