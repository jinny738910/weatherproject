package com.jinny.plancast.domain.todoUseCase

import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.UseCase

import com.jinny.plancast.data.entity.ToDoEntity

internal class GetToDoListUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(): List<ToDoEntity> {
        return toDoRepository.getToDoList()
    }

}