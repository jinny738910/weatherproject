package com.jinny.plancast.domain.todoUseCase

import com.jinny.plancast.data.entity.ToDoEntity
import com.jinny.plancast.data.repository.ToDoRepository
import com.jinny.plancast.domain.UseCase

internal class InsertToDoListUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(toDoList: List<ToDoEntity>) {
        return toDoRepository.insertToDoList(toDoList)
    }

}