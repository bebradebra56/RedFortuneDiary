package com.redifor.diarysof.rijer.data.utils

import android.util.Log
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RedFortuneDiaryPushToken {

    suspend fun redFortuneDiaryGetToken(
        redFortuneDiaryMaxAttempts: Int = 3,
        redFortuneDiaryDelayMs: Long = 1500
    ): String {

        repeat(redFortuneDiaryMaxAttempts - 1) {
            try {
                val redFortuneDiaryToken = FirebaseMessaging.getInstance().token.await()
                return redFortuneDiaryToken
            } catch (e: Exception) {
                Log.e(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Token error (attempt ${it + 1}): ${e.message}")
                delay(redFortuneDiaryDelayMs)
            }
        }

        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Token error final: ${e.message}")
            "null"
        }
    }


}