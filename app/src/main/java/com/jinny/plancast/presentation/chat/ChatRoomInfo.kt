package com.jinny.plancast.presentation.chat

data class ChatRoomInfo(
    val userId: String,
    val title: String,
    val date: String,
    val destination: String,
    val description: String,
    var message: String,
    val imageUrl: String,
) {
    constructor() : this("", "", "", "", "", "", "")
}