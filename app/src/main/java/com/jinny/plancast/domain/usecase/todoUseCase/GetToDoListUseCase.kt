package com.jinny.plancast.domain.usecase.todoUseCase

import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.usecase.UseCase

import com.jinny.plancast.data.local.entity.ToDoEntity
import javax.inject.Inject

class GetToDoListUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(): List<ToDoEntity> {
        return toDoRepository.getToDoList()
    }

}