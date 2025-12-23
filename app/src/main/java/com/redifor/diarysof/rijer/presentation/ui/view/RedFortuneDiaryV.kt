package com.redifor.diarysof.rijer.presentation.ui.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.redifor.diarysof.rijer.presentation.app.RedFortuneDiaryApplication
import com.redifor.diarysof.rijer.presentation.ui.load.RedFortuneDiaryLoadFragment
import org.koin.android.ext.android.inject

class RedFortuneDiaryV : Fragment(){

    private lateinit var redFortuneDiaryPhoto: Uri
    private var redFortuneDiaryFilePathFromChrome: ValueCallback<Array<Uri>>? = null

    private val redFortuneDiaryTakeFile: ActivityResultLauncher<PickVisualMediaRequest> = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        redFortuneDiaryFilePathFromChrome?.onReceiveValue(arrayOf(it ?: Uri.EMPTY))
        redFortuneDiaryFilePathFromChrome = null
    }

    private val redFortuneDiaryTakePhoto: ActivityResultLauncher<Uri> = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            redFortuneDiaryFilePathFromChrome?.onReceiveValue(arrayOf(redFortuneDiaryPhoto))
            redFortuneDiaryFilePathFromChrome = null
        } else {
            redFortuneDiaryFilePathFromChrome?.onReceiveValue(null)
            redFortuneDiaryFilePathFromChrome = null
        }
    }

    private val redFortuneDiaryDataStore by activityViewModels<RedFortuneDiaryDataStore>()


    private val redFortuneDiaryViFun by inject<RedFortuneDiaryViFun>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Fragment onCreate")
        CookieManager.getInstance().setAcceptCookie(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (redFortuneDiaryDataStore.redFortuneDiaryView.canGoBack()) {
                        redFortuneDiaryDataStore.redFortuneDiaryView.goBack()
                        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "WebView can go back")
                    } else if (redFortuneDiaryDataStore.redFortuneDiaryViList.size > 1) {
                        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "WebView can`t go back")
                        redFortuneDiaryDataStore.redFortuneDiaryViList.removeAt(redFortuneDiaryDataStore.redFortuneDiaryViList.lastIndex)
                        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "WebView list size ${redFortuneDiaryDataStore.redFortuneDiaryViList.size}")
                        redFortuneDiaryDataStore.redFortuneDiaryView.destroy()
                        val previousWebView = redFortuneDiaryDataStore.redFortuneDiaryViList.last()
                        redFortuneDiaryAttachWebViewToContainer(previousWebView)
                        redFortuneDiaryDataStore.redFortuneDiaryView = previousWebView
                    }
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (redFortuneDiaryDataStore.redFortuneDiaryIsFirstCreate) {
            redFortuneDiaryDataStore.redFortuneDiaryIsFirstCreate = false
            redFortuneDiaryDataStore.redFortuneDiaryContainerView = FrameLayout(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                id = View.generateViewId()
            }
            return redFortuneDiaryDataStore.redFortuneDiaryContainerView
        } else {
            return redFortuneDiaryDataStore.redFortuneDiaryContainerView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "onViewCreated")
        if (redFortuneDiaryDataStore.redFortuneDiaryViList.isEmpty()) {
            redFortuneDiaryDataStore.redFortuneDiaryView = RedFortuneDiaryVi(requireContext(), object :
                RedFortuneDiaryCallBack {
                override fun redFortuneDiaryHandleCreateWebWindowRequest(redFortuneDiaryVi: RedFortuneDiaryVi) {
                    redFortuneDiaryDataStore.redFortuneDiaryViList.add(redFortuneDiaryVi)
                    Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "WebView list size = ${redFortuneDiaryDataStore.redFortuneDiaryViList.size}")
                    Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "CreateWebWindowRequest")
                    redFortuneDiaryDataStore.redFortuneDiaryView = redFortuneDiaryVi
                    redFortuneDiaryVi.redFortuneDiarySetFileChooserHandler { callback ->
                        redFortuneDiaryHandleFileChooser(callback)
                    }
                    redFortuneDiaryAttachWebViewToContainer(redFortuneDiaryVi)
                }

            }, redFortuneDiaryWindow = requireActivity().window).apply {
                redFortuneDiarySetFileChooserHandler { callback ->
                    redFortuneDiaryHandleFileChooser(callback)
                }
            }
            redFortuneDiaryDataStore.redFortuneDiaryView.redFortuneDiaryFLoad(arguments?.getString(
                RedFortuneDiaryLoadFragment.RED_FORTUNE_DIARY_D) ?: "")
//            ejvview.fLoad("www.google.com")
            redFortuneDiaryDataStore.redFortuneDiaryViList.add(redFortuneDiaryDataStore.redFortuneDiaryView)
            redFortuneDiaryAttachWebViewToContainer(redFortuneDiaryDataStore.redFortuneDiaryView)
        } else {
            redFortuneDiaryDataStore.redFortuneDiaryViList.forEach { webView ->
                webView.redFortuneDiarySetFileChooserHandler { callback ->
                    redFortuneDiaryHandleFileChooser(callback)
                }
            }
            redFortuneDiaryDataStore.redFortuneDiaryView = redFortuneDiaryDataStore.redFortuneDiaryViList.last()

            redFortuneDiaryAttachWebViewToContainer(redFortuneDiaryDataStore.redFortuneDiaryView)
        }
        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "WebView list size = ${redFortuneDiaryDataStore.redFortuneDiaryViList.size}")
    }

    private fun redFortuneDiaryHandleFileChooser(callback: ValueCallback<Array<Uri>>?) {
        Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "handleFileChooser called, callback: ${callback != null}")

        redFortuneDiaryFilePathFromChrome = callback

        val listItems: Array<out String> = arrayOf("Select from file", "To make a photo")
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                0 -> {
                    Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Launching file picker")
                    redFortuneDiaryTakeFile.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                1 -> {
                    Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "Launching camera")
                    redFortuneDiaryPhoto = redFortuneDiaryViFun.redFortuneDiarySavePhoto()
                    redFortuneDiaryTakePhoto.launch(redFortuneDiaryPhoto)
                }
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Choose a method")
            .setItems(listItems, listener)
            .setCancelable(true)
            .setOnCancelListener {
                Log.d(RedFortuneDiaryApplication.RED_FORTUNE_DIARY_MAIN_TAG, "File chooser canceled")
                callback?.onReceiveValue(null)
                redFortuneDiaryFilePathFromChrome = null
            }
            .create()
            .show()
    }

    private fun redFortuneDiaryAttachWebViewToContainer(w: RedFortuneDiaryVi) {
        redFortuneDiaryDataStore.redFortuneDiaryContainerView.post {
            (w.parent as? ViewGroup)?.removeView(w)
            redFortuneDiaryDataStore.redFortuneDiaryContainerView.removeAllViews()
            redFortuneDiaryDataStore.redFortuneDiaryContainerView.addView(w)
        }
    }


}