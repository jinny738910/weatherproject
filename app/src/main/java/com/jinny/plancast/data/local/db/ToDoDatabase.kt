package com.jinny.plancast.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jinny.plancast.data.local.db.dao.ToDoDao
import com.jinny.plancast.data.local.entity.ToDoEntity

@Database(
    entities =  [ToDoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ToDoDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ToDoDataBase.db"
    }

    abstract fun toDoDao(): ToDoDao

}
