package com.fastcampus.part5.chapter01.domain.todoUseCase

import com.fastcampus.part5.chapter01.data.entity.ToDoEntity
import com.fastcampus.part5.chapter01.data.repository.ToDoRepository
import com.fastcampus.part5.chapter01.domain.UseCase

internal class UpdateToDoUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(toDoEntity: ToDoEntity) {
        return toDoRepository.updateToDoItem(toDoEntity)
    }

}
