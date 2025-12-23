package com.redifor.diarysof.rijer

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication

class RedFortuneDiaryGlobalLayoutUtil {

    private var redFortuneDiaryMChildOfContent: View? = null
    private var redFortuneDiaryUsableHeightPrevious = 0

    fun redFortuneDiaryAssistActivity(activity: Activity) {
        val content = activity.findViewById<FrameLayout>(android.R.id.content)
        redFortuneDiaryMChildOfContent = content.getChildAt(0)

        redFortuneDiaryMChildOfContent?.viewTreeObserver?.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent(activity)
        }
    }

    private fun possiblyResizeChildOfContent(activity: Activity) {
        val redFortuneDiaryUsableHeightNow = redFortuneDiaryComputeUsableHeight()
        if (redFortuneDiaryUsableHeightNow != redFortuneDiaryUsableHeightPrevious) {
            val redFortuneDiaryUsableHeightSansKeyboard = redFortuneDiaryMChildOfContent?.rootView?.height ?: 0
            val redFortuneDiaryHeightDifference = redFortuneDiaryUsableHeightSansKeyboard - redFortuneDiaryUsableHeightNow

            if (redFortuneDiaryHeightDifference > (redFortuneDiaryUsableHeightSansKeyboard / 4)) {
                activity.window.setSoftInputMode(RedFortuneDiaryApplication.redFortuneDiaryInputMode)
            } else {
                activity.window.setSoftInputMode(RedFortuneDiaryApplication.redFortuneDiaryInputMode)
            }
//            mChildOfContent?.requestLayout()
            redFortuneDiaryUsableHeightPrevious = redFortuneDiaryUsableHeightNow
        }
    }

    private fun redFortuneDiaryComputeUsableHeight(): Int {
        val r = Rect()
        redFortuneDiaryMChildOfContent?.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top  // Visible height без status bar
    }
}