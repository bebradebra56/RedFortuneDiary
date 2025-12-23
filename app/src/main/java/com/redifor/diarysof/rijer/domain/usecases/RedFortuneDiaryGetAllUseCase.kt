package com.redifor.diarysof.rijer.domain.usecases

import android.util.Log
import com.redifor.diarysof.rijer.data.repo.RedFortuneDiaryRepository
import com.redifor.diarysof.rijer.data.utils.RedFortuneDiaryPushToken
import com.redifor.diarysof.rijer.data.utils.RedFortuneDiarySystemService
import com.redifor.diarysof.rijer.domain.model.RedFortuneDiaryEntity
import com.redifor.diarysof.rijer.domain.model.RedFortuneDiaryParam
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication

class RedFortuneDiaryGetAllUseCase(
    private val redFortuneDiaryRepository: RedFortuneDiaryRepository,
    private val redFortuneDiarySystemService: RedFortuneDiarySystemService,
    private val redFortuneDiaryPushToken: RedFortuneDiaryPushToken,
) {
    suspend operator fun invoke(conversion: MutableMap<String, Any>?) : RedFortuneDiaryEntity?{
        val params = RedFortuneDiaryParam(
            redFortuneDiaryLocale = redFortuneDiarySystemService.redFortuneDiaryGetLocale(),
            redFortuneDiaryPushToken = redFortuneDiaryPushToken.redFortuneDiaryGetToken(),
            redFortuneDiaryAfId = redFortuneDiarySystemService.redFortuneDiaryGetAppsflyerId()
        )
        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Params for request: $params")
        return redFortuneDiaryRepository.redFortuneDiaryGetClient(params, conversion)
    }



}