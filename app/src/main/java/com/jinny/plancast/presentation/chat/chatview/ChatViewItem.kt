package com.jinny.plancast.presentation.chat.chatview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatViewItem(
    val userId: String,
    val title: String,
    val date: String,
    val destination: String,
    val description: String,
    var message: String,
    val imageUrl: String,
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "")
}