package com.redifor.diarysof.rijer.data.shar

import android.content.Context
import androidx.core.content.edit

class RedFortuneDiarySharedPreference(context: Context) {
    private val redFortuneDiaryPrefs = context.getSharedPreferences("redFortuneDiarySharedPrefsAb", Context.MODE_PRIVATE)

    var redFortuneDiarySavedUrl: String
        get() = redFortuneDiaryPrefs.getString(RED_FORTUNE_DIARY_SAVED_URL, "") ?: ""
        set(value) = redFortuneDiaryPrefs.edit { putString(RED_FORTUNE_DIARY_SAVED_URL, value) }

    var redFortuneDiaryExpired : Long
        get() = redFortuneDiaryPrefs.getLong(RED_FORTUNE_DIARY_EXPIRED, 0L)
        set(value) = redFortuneDiaryPrefs.edit { putLong(RED_FORTUNE_DIARY_EXPIRED, value) }

    var redFortuneDiaryAppState: Int
        get() = redFortuneDiaryPrefs.getInt(RED_FORTUNE_DIARY_APPLICATION_STATE, 0)
        set(value) = redFortuneDiaryPrefs.edit { putInt(RED_FORTUNE_DIARY_APPLICATION_STATE, value) }

    var redFortuneDiaryNotificationRequest: Long
        get() = redFortuneDiaryPrefs.getLong(RED_FORTUNE_DIARY_NOTIFICAITON_REQUEST, 0L)
        set(value) = redFortuneDiaryPrefs.edit { putLong(RED_FORTUNE_DIARY_NOTIFICAITON_REQUEST, value) }

    var redFortuneDiaryNotificationRequestedBefore: Boolean
        get() = redFortuneDiaryPrefs.getBoolean(RED_FORTUNE_DIARY_NOTIFICATION_REQUEST_BEFORE, false)
        set(value) = redFortuneDiaryPrefs.edit { putBoolean(
            RED_FORTUNE_DIARY_NOTIFICATION_REQUEST_BEFORE, value) }

    companion object {
        private const val RED_FORTUNE_DIARY_SAVED_URL = "redFortuneDiarySavedUrl"
        private const val RED_FORTUNE_DIARY_EXPIRED = "redFortuneDiaryExpired"
        private const val RED_FORTUNE_DIARY_APPLICATION_STATE = "redFortuneDiaryApplicationState"
        private const val RED_FORTUNE_DIARY_NOTIFICAITON_REQUEST = "redFortuneDiaryNotificationRequest"
        private const val RED_FORTUNE_DIARY_NOTIFICATION_REQUEST_BEFORE = "redFortuneDiaryNotificationRequestedBefore"
    }
}