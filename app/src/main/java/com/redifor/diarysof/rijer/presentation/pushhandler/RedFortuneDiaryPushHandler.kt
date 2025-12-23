package com.redifor.diarysof.rijer.presentation.pushhandler

import android.os.Bundle
import android.util.Log
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication

class RedFortuneDiaryPushHandler {
    fun redFortuneDiaryHandlePush(extras: Bundle?) {
        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Extras from Push = ${extras?.keySet()}")
        if (extras != null) {
            val map = redFortuneDiaryBundleToMap(extras)
            Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Map from Push = $map")
            map?.let {
                if (map.containsKey("url")) {
                    RedFortuneDiaryApplication.RED_FORTUNE_DIARY_FB_LI = map["url"]
                    Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "UrlFromActivity = $map")
                }
            }
        } else {
            Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Push data no!")
        }
    }

    private fun redFortuneDiaryBundleToMap(extras: Bundle): Map<String, String?>? {
        val map: MutableMap<String, String?> = HashMap()
        val ks = extras.keySet()
        val iterator: Iterator<String> = ks.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = extras.getString(key)
        }
        return map
    }

}