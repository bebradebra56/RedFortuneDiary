package com.redifor.diarysof.rijer.presentation.notificiation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.redifor.diarysof.R
import com.redifor.diarysof.RedFortuneDiaryActivity
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication

private const val RED_FORTUNE_DIARY_CHANNEL_ID = "red_fortune_diary_notifications"
private const val RED_FORTUNE_DIARY_CHANNEL_NAME = "RedFortuneDiary Notifications"
private const val RED_FORTUNE_DIARY_NOT_TAG = "RedFortuneDiary"

class RedFortuneDiaryPushService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Обработка notification payload
        remoteMessage.notification?.let {
            if (remoteMessage.data.contains("url")) {
                redFortuneDiaryShowNotification(it.title ?: RED_FORTUNE_DIARY_NOT_TAG, it.body ?: "", data = remoteMessage.data["url"])
            } else {
                redFortuneDiaryShowNotification(it.title ?: RED_FORTUNE_DIARY_NOT_TAG, it.body ?: "", data = null)
            }
        }

        // Обработка data payload
        if (remoteMessage.data.isNotEmpty()) {
            redFortuneDiaryHandleDataPayload(remoteMessage.data)
        }
    }

    private fun redFortuneDiaryShowNotification(title: String, message: String, data: String?) {
        val redFortuneDiaryNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                RED_FORTUNE_DIARY_CHANNEL_ID,
                RED_FORTUNE_DIARY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            redFortuneDiaryNotificationManager.createNotificationChannel(channel)
        }

        val redFortuneDiaryIntent = Intent(this, RedFortuneDiaryActivity::class.java).apply {
            putExtras(bundleOf(
                "url" to data
            ))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val redFortuneDiaryPendingIntent = PendingIntent.getActivity(
            this,
            0,
            redFortuneDiaryIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val redFortuneDiaryNotification = NotificationCompat.Builder(this, RED_FORTUNE_DIARY_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.red_fortune_diary_noti_ic)
            .setAutoCancel(true)
            .setContentIntent(redFortuneDiaryPendingIntent)
            .build()

        redFortuneDiaryNotificationManager.notify(System.currentTimeMillis().toInt(), redFortuneDiaryNotification)
    }

    private fun redFortuneDiaryHandleDataPayload(data: Map<String, String>) {
        data.forEach { (key, value) ->
            Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Data key=$key value=$value")
        }
    }
}