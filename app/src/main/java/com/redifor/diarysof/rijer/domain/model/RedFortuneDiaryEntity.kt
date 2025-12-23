package com.redifor.diarysof.rijer.domain.model

import com.google.gson.annotations.SerializedName


data class RedFortuneDiaryEntity (
    @SerializedName("ok")
    val redFortuneDiaryOk: String,
    @SerializedName("url")
    val redFortuneDiaryUrl: String,
    @SerializedName("expires")
    val redFortuneDiaryExpires: Long,
)