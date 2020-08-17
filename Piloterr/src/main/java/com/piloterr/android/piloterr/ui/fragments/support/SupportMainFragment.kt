package com.piloterr.android.piloterr.ui.fragments.support

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.FAQRepository
import com.piloterr.android.piloterr.databinding.FragmentSupportMainBinding
import com.piloterr.android.piloterr.helpers.AppConfigManager
import com.piloterr.android.piloterr.helpers.AppTestingLevel
import com.piloterr.android.piloterr.helpers.DeviceName
import com.piloterr.android.piloterr.helpers.MainNavigationController
import com.piloterr.android.piloterr.modules.AppModule
import com.piloterr.android.piloterr.ui.fragments.BaseMainFragment
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Named


class SupportMainFragment : BaseMainFragment() {
    private var deviceInfo: DeviceName.DeviceInfo? = null

    private lateinit var binding: FragmentSupportMainBinding
    @field:[Inject Named(AppModule.NAMED_USER_ID)]
    lateinit var userId: String
    @Inject
    lateinit var faqRepository: FAQRepository
    @Inject
    lateinit var appConfigManager: AppConfigManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        hidesToolbar = true
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSupportMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.usingPiloterrWrapper.setOnClickListener {
            MainNavigationController.navigate(R.id.FAQOverviewFragment)
        }
        binding.bugsFixesWrapper.setOnClickListener {
            MainNavigationController.navigate(R.id.bugFixFragment)
        }
        binding.suggestionsFeedbackWrapper.setOnClickListener {
            if (appConfigManager.feedbackURL().isNotBlank()) {
                val uriUrl = appConfigManager.feedbackURL().toUri()
                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                startActivity(launchBrowser)
            }
        }

        compositeSubscription.add(Completable.fromAction {
            deviceInfo = DeviceName.getDeviceInfo(context)
        }.subscribe())

        binding.resetTutorialButton.setOnClickListener {
            userRepository.resetTutorial(user)
        }
    }

    override fun onDestroy() {
        faqRepository.close()
        super.onDestroy()
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }
}