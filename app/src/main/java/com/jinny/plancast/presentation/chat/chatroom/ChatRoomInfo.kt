package com.jinny.plancast.presentation.chat.chatroom

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoomInfo(
    val userId: String,
    val title: String,
    val date: String,
    val destination: String,
    val description: String,
    var message: String,
    val imageUrl: String,
    val invitedUser: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", "")
}