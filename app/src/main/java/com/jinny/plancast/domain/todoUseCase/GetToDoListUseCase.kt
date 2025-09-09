package com.fastcampus.part5.chapter01.domain.todoUseCase

import com.fastcampus.part5.chapter01.data.repository.ToDoRepository
import com.fastcampus.part5.chapter01.domain.UseCase

import com.fastcampus.part5.chapter01.data.entity.ToDoEntity

internal class GetToDoListUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(): List<ToDoEntity> {
        return toDoRepository.getToDoList()
    }

}