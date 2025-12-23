package com.redifor.diarysof.rijer.presentation.ui.view

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel

class RedFortuneDiaryDataStore : ViewModel(){
    val redFortuneDiaryViList: MutableList<RedFortuneDiaryVi> = mutableListOf()
    var redFortuneDiaryIsFirstCreate = true
    @SuppressLint("StaticFieldLeak")
    lateinit var redFortuneDiaryContainerView: FrameLayout
    @SuppressLint("StaticFieldLeak")
    lateinit var redFortuneDiaryView: RedFortuneDiaryVi

}