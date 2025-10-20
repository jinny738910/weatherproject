package com.jinny.plancast.data.local.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: String,
    val destination: String,
    val description: String,
    val image: String,
    val hasCompleted: Boolean = false,
    val isClimate: Boolean = false,
    val isLocation: Boolean = false,
    val isFinancial: Boolean = false,
    val isRepeat: Boolean = false,
    val isLock: Boolean = false
)
