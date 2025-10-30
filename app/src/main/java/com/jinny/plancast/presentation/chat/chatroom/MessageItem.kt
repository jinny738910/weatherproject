package com.jinny.plancast.presentation.chat.chatroom

data class MessageItem(
    val userId: String,
    var name: String,
    val isUser: Boolean,
    var message: String,
    val timestamp: String
)