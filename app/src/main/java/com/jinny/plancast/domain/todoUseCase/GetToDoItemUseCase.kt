package com.fastcampus.part5.chapter01.domain.todoUseCase

import com.fastcampus.part5.chapter01.data.entity.ToDoEntity
import com.fastcampus.part5.chapter01.data.repository.ToDoRepository
import com.fastcampus.part5.chapter01.domain.UseCase

internal class GetToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(id: Long): ToDoEntity? {
        return toDoRepository.getToDoItem(id)
    }

}
