package com.redifor.diarysof

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.redifor.diarysof.rijer.RedFortuneDiaryGlobalLayoutUtil
import com.redifor.diarysof.rijer.redFortuneDiarySetupSystemBars
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication
import com.redifor.diarysof.rijer.presentation.pushhandler.RedFortuneDiaryPushHandler
import org.koin.android.ext.android.inject

class RedFortuneDiaryActivity : AppCompatActivity() {

    private val redFortuneDiaryPushHandler by inject<RedFortuneDiaryPushHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        redFortuneDiarySetupSystemBars()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_red_fortune_diary)

        val redFortuneDiaryRootView = findViewById<View>(android.R.id.content)
        RedFortuneDiaryGlobalLayoutUtil().redFortuneDiaryAssistActivity(this)
        ViewCompat.setOnApplyWindowInsetsListener(redFortuneDiaryRootView) { redFortuneDiaryView, redFortuneDiaryInsets ->
            val redFortuneDiarySystemBars = redFortuneDiaryInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val redFortuneDiaryDisplayCutout = redFortuneDiaryInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val redFortuneDiaryIme = redFortuneDiaryInsets.getInsets(WindowInsetsCompat.Type.ime())


            val redFortuneDiaryTopPadding = maxOf(redFortuneDiarySystemBars.top, redFortuneDiaryDisplayCutout.top)
            val redFortuneDiaryLeftPadding = maxOf(redFortuneDiarySystemBars.left, redFortuneDiaryDisplayCutout.left)
            val redFortuneDiaryRightPadding = maxOf(redFortuneDiarySystemBars.right, redFortuneDiaryDisplayCutout.right)
            window.setSoftInputMode(RedFortuneDiaryApplication.redFortuneDiaryInputMode)

            if (window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) {
                Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "ADJUST PUN")
                val redFortuneDiaryBottomInset = maxOf(redFortuneDiarySystemBars.bottom, redFortuneDiaryDisplayCutout.bottom)

                redFortuneDiaryView.setPadding(redFortuneDiaryLeftPadding, redFortuneDiaryTopPadding, redFortuneDiaryRightPadding, 0)

                redFortuneDiaryView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = redFortuneDiaryBottomInset
                }
            } else {
                Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "ADJUST RESIZE")

                val redFortuneDiaryBottomInset = maxOf(redFortuneDiarySystemBars.bottom, redFortuneDiaryDisplayCutout.bottom, redFortuneDiaryIme.bottom)

                redFortuneDiaryView.setPadding(redFortuneDiaryLeftPadding, redFortuneDiaryTopPadding, redFortuneDiaryRightPadding, 0)

                redFortuneDiaryView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = redFortuneDiaryBottomInset
                }
            }



            WindowInsetsCompat.CONSUMED
        }
        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Activity onCreate()")
        redFortuneDiaryPushHandler.redFortuneDiaryHandlePush(intent.extras)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            redFortuneDiarySetupSystemBars()
        }
    }

    override fun onResume() {
        super.onResume()
        redFortuneDiarySetupSystemBars()
    }
}