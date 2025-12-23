package com.redifor.diarysof.rijer.domain.model

import com.google.gson.annotations.SerializedName


private const val RED_FORTUNE_DIARY_A = "com.redifor.diarysof"
private const val RED_FORTUNE_DIARY_B = "redfortubediary"
data class RedFortuneDiaryParam (
    @SerializedName("af_id")
    val redFortuneDiaryAfId: String,
    @SerializedName("bundle_id")
    val redFortuneDiaryBundleId: String = RED_FORTUNE_DIARY_A,
    @SerializedName("os")
    val redFortuneDiaryOs: String = "Android",
    @SerializedName("store_id")
    val redFortuneDiaryStoreId: String = RED_FORTUNE_DIARY_A,
    @SerializedName("locale")
    val redFortuneDiaryLocale: String,
    @SerializedName("push_token")
    val redFortuneDiaryPushToken: String,
    @SerializedName("firebase_project_id")
    val redFortuneDiaryFirebaseProjectId: String = RED_FORTUNE_DIARY_B,

    )