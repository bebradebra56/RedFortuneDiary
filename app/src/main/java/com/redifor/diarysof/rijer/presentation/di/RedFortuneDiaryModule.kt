package com.redifor.diarysof.rijer.presentation.di

import com.redifor.diarysof.rijer.data.repo.RedFortuneDiaryRepository
import com.redifor.diarysof.rijer.data.shar.RedFortuneDiarySharedPreference
import com.redifor.diarysof.rijer.data.utils.RedFortuneDiaryPushToken
import com.redifor.diarysof.rijer.data.utils.RedFortuneDiarySystemService
import com.redifor.diarysof.rijer.domain.usecases.RedFortuneDiaryGetAllUseCase
import com.redifor.diarysof.rijer.presentation.pushhandler.RedFortuneDiaryPushHandler
import com.redifor.diarysof.rijer.presentation.ui.load.RedFortuneDiaryLoadViewModel
import com.redifor.diarysof.rijer.presentation.ui.view.RedFortuneDiaryViFun
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val redFortuneDiaryModule = module {
    factory {
        RedFortuneDiaryPushHandler()
    }
    single {
        RedFortuneDiaryRepository()
    }
    single {
        RedFortuneDiarySharedPreference(get())
    }
    factory {
        RedFortuneDiaryPushToken()
    }
    factory {
        RedFortuneDiarySystemService(get())
    }
    factory {
        RedFortuneDiaryGetAllUseCase(
            get(), get(), get()
        )
    }
    factory {
        RedFortuneDiaryViFun(get())
    }
    viewModel {
        RedFortuneDiaryLoadViewModel(get(), get(), get())
    }
}