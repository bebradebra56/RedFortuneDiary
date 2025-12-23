package com.redifor.diarysof.rijer.presentation.ui.load

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.redifor.diarysof.MainActivity
import com.redifor.diarysof.R
import com.redifor.diarysof.databinding.FragmentLoadRedFortuneDiaryBinding
import com.redifor.diarysof.rijer.data.shar.RedFortuneDiarySharedPreference
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class RedFortuneDiaryLoadFragment : Fragment(R.layout.fragment_load_red_fortune_diary) {
    private lateinit var redFortuneDiaryLoadBinding: FragmentLoadRedFortuneDiaryBinding

    private val redFortuneDiaryLoadViewModel by viewModel<RedFortuneDiaryLoadViewModel>()

    private val redFortuneDiarySharedPreference by inject<RedFortuneDiarySharedPreference>()

    private var redFortuneDiaryUrl = ""

    private val redFortuneDiaryRequestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            redFortuneDiaryNavigateToSuccess(redFortuneDiaryUrl)
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                redFortuneDiarySharedPreference.redFortuneDiaryNotificationRequest =
                    (System.currentTimeMillis() / 1000) + 259200
                redFortuneDiaryNavigateToSuccess(redFortuneDiaryUrl)
            } else {
                redFortuneDiaryNavigateToSuccess(redFortuneDiaryUrl)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        redFortuneDiaryLoadBinding = FragmentLoadRedFortuneDiaryBinding.bind(view)

        redFortuneDiaryLoadBinding.redFortuneDiaryGrandButton.setOnClickListener {
            val redFortuneDiaryPermission = Manifest.permission.POST_NOTIFICATIONS
            redFortuneDiaryRequestNotificationPermission.launch(redFortuneDiaryPermission)
            redFortuneDiarySharedPreference.redFortuneDiaryNotificationRequestedBefore = true
        }

        redFortuneDiaryLoadBinding.redFortuneDiarySkipButton.setOnClickListener {
            redFortuneDiarySharedPreference.redFortuneDiaryNotificationRequest =
                (System.currentTimeMillis() / 1000) + 259200
            redFortuneDiaryNavigateToSuccess(redFortuneDiaryUrl)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                redFortuneDiaryLoadViewModel.redFortuneDiaryHomeScreenState.collect {
                    when (it) {
                        is RedFortuneDiaryLoadViewModel.RedFortuneDiaryHomeScreenState.RedFortuneDiaryLoading -> {

                        }

                        is RedFortuneDiaryLoadViewModel.RedFortuneDiaryHomeScreenState.RedFortuneDiaryError -> {
                            requireActivity().startActivity(
                                Intent(
                                    requireContext(),
                                    MainActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }

                        is RedFortuneDiaryLoadViewModel.RedFortuneDiaryHomeScreenState.RedFortuneDiarySuccess -> {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                                val redFortuneDiaryPermission = Manifest.permission.POST_NOTIFICATIONS
                                val redFortuneDiaryPermissionRequestedBefore = redFortuneDiarySharedPreference.redFortuneDiaryNotificationRequestedBefore

                                if (ContextCompat.checkSelfPermission(requireContext(), redFortuneDiaryPermission) == PackageManager.PERMISSION_GRANTED) {
                                    redFortuneDiaryNavigateToSuccess(it.data)
                                } else if (!redFortuneDiaryPermissionRequestedBefore && (System.currentTimeMillis() / 1000 > redFortuneDiarySharedPreference.redFortuneDiaryNotificationRequest)) {
                                    // первый раз — показываем UI для запроса
                                    redFortuneDiaryLoadBinding.redFortuneDiaryNotiGroup.visibility = View.VISIBLE
                                    redFortuneDiaryLoadBinding.redFortuneDiaryLoadingGroup.visibility = View.GONE
                                    redFortuneDiaryUrl = it.data
                                } else if (shouldShowRequestPermissionRationale(redFortuneDiaryPermission)) {
                                    // временный отказ — через 3 дня можно показать
                                    if (System.currentTimeMillis() / 1000 > redFortuneDiarySharedPreference.redFortuneDiaryNotificationRequest) {
                                        redFortuneDiaryLoadBinding.redFortuneDiaryNotiGroup.visibility = View.VISIBLE
                                        redFortuneDiaryLoadBinding.redFortuneDiaryLoadingGroup.visibility = View.GONE
                                        redFortuneDiaryUrl = it.data
                                    } else {
                                        redFortuneDiaryNavigateToSuccess(it.data)
                                    }
                                } else {
                                    // навсегда отклонено — просто пропускаем
                                    redFortuneDiaryNavigateToSuccess(it.data)
                                }
                            } else {
                                redFortuneDiaryNavigateToSuccess(it.data)
                            }
                        }

                        RedFortuneDiaryLoadViewModel.RedFortuneDiaryHomeScreenState.RedFortuneDiaryNotInternet -> {
                            redFortuneDiaryLoadBinding.redFortuneDiaryStateGroup.visibility = View.VISIBLE
                            redFortuneDiaryLoadBinding.redFortuneDiaryLoadingGroup.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    private fun redFortuneDiaryNavigateToSuccess(data: String) {
        findNavController().navigate(
            R.id.action_redFortuneDiaryLoadFragment_to_redFortuneDiaryV,
            bundleOf(RED_FORTUNE_DIARY_D to data)
        )
    }

    companion object {
        const val RED_FORTUNE_DIARY_D = "redFortuneDiaryData"
    }
}