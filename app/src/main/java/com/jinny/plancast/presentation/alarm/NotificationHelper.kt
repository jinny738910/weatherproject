package com.jinny.plancast.presentation.alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.jinny.plancast.R

// 알림 채널을 생성하고 실제 알림을 사용자에게 보여주는 유틸리티 클래스
@SuppressLint("NewApi")
class NotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "todo_reminder_channel"
        private const val CHANNEL_NAME = "할 일 알림"
    }

    init {
        // Android 8.0 (Oreo) 이상에서는 알림 채널이 필수
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "지정한 할 일에 대한 알림을 보냅니다."
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(title: String, message: String, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_alarm_24) // 알림 아이콘
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // 여러 줄 텍스트 표시

        notificationManager.notify(notificationId, builder.build())
    }
}