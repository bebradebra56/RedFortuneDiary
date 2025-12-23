package com.redifor.diarysof.rijer.presentation.ui.load

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redifor.diarysof.rijer.data.shar.RedFortuneDiarySharedPreference
import com.redifor.diarysof.rijer.data.utils.RedFortuneDiarySystemService
import com.redifor.diarysof.rijer.domain.usecases.RedFortuneDiaryGetAllUseCase
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryAppsFlyerState
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RedFortuneDiaryLoadViewModel(
    private val redFortuneDiaryGetAllUseCase: RedFortuneDiaryGetAllUseCase,
    private val redFortuneDiarySharedPreference: RedFortuneDiarySharedPreference,
    private val redFortuneDiarySystemService: RedFortuneDiarySystemService
) : ViewModel() {

    private val _redFortuneDiaryHomeScreenState: MutableStateFlow<RedFortuneDiaryHomeScreenState> =
        MutableStateFlow(RedFortuneDiaryHomeScreenState.RedFortuneDiaryLoading)
    val redFortuneDiaryHomeScreenState = _redFortuneDiaryHomeScreenState.asStateFlow()

    private var redFortuneDiaryGetApps = false


    init {
        viewModelScope.launch {
            when (redFortuneDiarySharedPreference.redFortuneDiaryAppState) {
                0 -> {
                    if (redFortuneDiarySystemService.redFortuneDiaryIsOnline()) {
                        RedFortuneDiaryApplication.redFortuneDiaryConversionFlow.collect {
                            when(it) {
                                RedFortuneDiaryAppsFlyerState.RedFortuneDiaryDefault -> {}
                                RedFortuneDiaryAppsFlyerState.RedFortuneDiaryError -> {
                                    redFortuneDiarySharedPreference.redFortuneDiaryAppState = 2
                                    _redFortuneDiaryHomeScreenState.value =
                                        RedFortuneDiaryHomeScreenState.RedFortuneDiaryError
                                    redFortuneDiaryGetApps = true
                                }
                                is RedFortuneDiaryAppsFlyerState.RedFortuneDiarySuccess -> {
                                    if (!redFortuneDiaryGetApps) {
                                        redFortuneDiaryGetData(it.redFortuneDiaryData)
                                        redFortuneDiaryGetApps = true
                                    }
                                }
                            }
                        }
                    } else {
                        _redFortuneDiaryHomeScreenState.value =
                            RedFortuneDiaryHomeScreenState.RedFortuneDiaryNotInternet
                    }
                }
                1 -> {
                    if (redFortuneDiarySystemService.redFortuneDiaryIsOnline()) {
                        if (RedFortuneDiaryApplication.RED_FORTUNE_DIARY_FB_LI != null) {
                            _redFortuneDiaryHomeScreenState.value =
                                RedFortuneDiaryHomeScreenState.RedFortuneDiarySuccess(
                                    RedFortuneDiaryApplication.RED_FORTUNE_DIARY_FB_LI.toString()
                                )
                        } else if (System.currentTimeMillis() / 1000 > redFortuneDiarySharedPreference.redFortuneDiaryExpired) {
                            Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Current time more then expired, repeat request")
                            RedFortuneDiaryApplication.redFortuneDiaryConversionFlow.collect {
                                when(it) {
                                    RedFortuneDiaryAppsFlyerState.RedFortuneDiaryDefault -> {}
                                    RedFortuneDiaryAppsFlyerState.RedFortuneDiaryError -> {
                                        _redFortuneDiaryHomeScreenState.value =
                                            RedFortuneDiaryHomeScreenState.RedFortuneDiarySuccess(
                                                redFortuneDiarySharedPreference.redFortuneDiarySavedUrl
                                            )
                                        redFortuneDiaryGetApps = true
                                    }
                                    is RedFortuneDiaryAppsFlyerState.RedFortuneDiarySuccess -> {
                                        if (!redFortuneDiaryGetApps) {
                                            redFortuneDiaryGetData(it.redFortuneDiaryData)
                                            redFortuneDiaryGetApps = true
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Current time less then expired, use saved url")
                            _redFortuneDiaryHomeScreenState.value =
                                RedFortuneDiaryHomeScreenState.RedFortuneDiarySuccess(
                                    redFortuneDiarySharedPreference.redFortuneDiarySavedUrl
                                )
                        }
                    } else {
                        _redFortuneDiaryHomeScreenState.value =
                            RedFortuneDiaryHomeScreenState.RedFortuneDiaryNotInternet
                    }
                }
                2 -> {
                    _redFortuneDiaryHomeScreenState.value =
                        RedFortuneDiaryHomeScreenState.RedFortuneDiaryError
                }
            }
        }
    }


    private suspend fun redFortuneDiaryGetData(conversation: MutableMap<String, Any>?) {
        val redFortuneDiaryData = redFortuneDiaryGetAllUseCase.invoke(conversation)
        if (redFortuneDiarySharedPreference.redFortuneDiaryAppState == 0) {
            if (redFortuneDiaryData == null) {
                redFortuneDiarySharedPreference.redFortuneDiaryAppState = 2
                _redFortuneDiaryHomeScreenState.value =
                    RedFortuneDiaryHomeScreenState.RedFortuneDiaryError
            } else {
                redFortuneDiarySharedPreference.redFortuneDiaryAppState = 1
                redFortuneDiarySharedPreference.apply {
                    redFortuneDiaryExpired = redFortuneDiaryData.redFortuneDiaryExpires
                    redFortuneDiarySavedUrl = redFortuneDiaryData.redFortuneDiaryUrl
                }
                _redFortuneDiaryHomeScreenState.value =
                    RedFortuneDiaryHomeScreenState.RedFortuneDiarySuccess(redFortuneDiaryData.redFortuneDiaryUrl)
            }
        } else  {
            if (redFortuneDiaryData == null) {
                _redFortuneDiaryHomeScreenState.value =
                    RedFortuneDiaryHomeScreenState.RedFortuneDiarySuccess(
                        redFortuneDiarySharedPreference.redFortuneDiarySavedUrl
                    )
            } else {
                redFortuneDiarySharedPreference.apply {
                    redFortuneDiaryExpired = redFortuneDiaryData.redFortuneDiaryExpires
                    redFortuneDiarySavedUrl = redFortuneDiaryData.redFortuneDiaryUrl
                }
                _redFortuneDiaryHomeScreenState.value =
                    RedFortuneDiaryHomeScreenState.RedFortuneDiarySuccess(redFortuneDiaryData.redFortuneDiaryUrl)
            }
        }
    }


    sealed class RedFortuneDiaryHomeScreenState {
        data object RedFortuneDiaryLoading : RedFortuneDiaryHomeScreenState()
        data object RedFortuneDiaryError : RedFortuneDiaryHomeScreenState()
        data class RedFortuneDiarySuccess(val data: String) : RedFortuneDiaryHomeScreenState()
        data object RedFortuneDiaryNotInternet: RedFortuneDiaryHomeScreenState()
    }
}