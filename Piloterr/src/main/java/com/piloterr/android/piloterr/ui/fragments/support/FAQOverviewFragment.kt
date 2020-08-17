package com.piloterr.android.piloterr.ui.fragments.support

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.FAQRepository
import com.piloterr.android.piloterr.databinding.FragmentFaqOverviewBinding
import com.piloterr.android.piloterr.databinding.SupportFaqItemBinding
import com.piloterr.android.piloterr.extensions.layoutInflater
import com.piloterr.android.piloterr.helpers.MainNavigationController
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.ui.fragments.BaseMainFragment
import com.piloterr.android.piloterr.ui.helpers.setMarkdown
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import io.reactivex.functions.Consumer
import javax.inject.Inject

class FAQOverviewFragment : BaseMainFragment() {

    private lateinit var binding: FragmentFaqOverviewBinding

    @Inject
    lateinit var faqRepository: FAQRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        hidesToolbar = true
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFaqOverviewBinding.inflate(inflater, container, false)
        binding.healthSection.findViewById<ImageView>(R.id.icon_view).setImageBitmap(PiloterrIconsHelper.imageOfHeartLarge())
        binding.experienceSection.findViewById<ImageView>(R.id.icon_view).setImageBitmap(PiloterrIconsHelper.imageOfExperienceReward())
        binding.manaSection.findViewById<ImageView>(R.id.icon_view).setImageBitmap(PiloterrIconsHelper.imageOfMagicLarge())
        binding.goldSection.findViewById<ImageView>(R.id.icon_view).setImageBitmap(PiloterrIconsHelper.imageOfGoldReward())
        binding.gemsSection.findViewById<ImageView>(R.id.icon_view).setImageBitmap(PiloterrIconsHelper.imageOfGem())
        binding.hourglassesSection.findViewById<ImageView>(R.id.icon_view).setImageBitmap(PiloterrIconsHelper.imageOfHourglassLarge())
        binding.statsSection.findViewById<ImageView>(R.id.icon_view).setImageBitmap(PiloterrIconsHelper.imageOfStats())

        binding.moreHelpTextView.setMarkdown(context?.getString(R.string.need_help_header_description, "[Piloterr Help Guild](https://piloterr.com/groups/guild/39bed861-f2d6-4f8e-8420-eae83e569281)"))
        binding.moreHelpTextView.setOnClickListener { MainNavigationController.navigate(R.id.guildFragment, bundleOf("groupID" to "39bed861-f2d6-4f8e-8420-eae83e569281")) }
        binding.moreHelpTextView.movementMethod = LinkMovementMethod.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.loadArticles()
    }

    override fun onDestroy() {
        faqRepository.close()
        super.onDestroy()
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    private fun loadArticles() {
        compositeSubscription.add(faqRepository.getArticles().subscribe(Consumer {
            val context = context ?: return@Consumer
            for (article in it) {
                val binding = SupportFaqItemBinding.inflate(context.layoutInflater, binding.faqLinearLayout, true)
                binding.textView.text = article.question
                binding.root.setOnClickListener {
                    MainNavigationController.navigate(FAQOverviewFragmentDirections.openFAQDetail(article.position ?: 0))
                }
            }
        }, RxErrorHandler.handleEmptyError()))
    }

}
