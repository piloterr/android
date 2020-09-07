package com.piloterr.android.piloterr.ui.views.dialogs

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.databinding.DialogCompletedQuestContentBinding
import com.piloterr.android.piloterr.extensions.layoutInflater
import com.piloterr.android.piloterr.models.inventory.QuestContent
import com.piloterr.android.piloterr.models.inventory.QuestDropItem
import com.piloterr.android.piloterr.ui.helpers.DataBindingUtils
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper

class QuestCompletedDialogContent : LinearLayout {


    private lateinit var binding: DialogCompletedQuestContentBinding

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupView()
    }

    private fun setupView() {
        orientation = VERTICAL
        gravity = Gravity.CENTER

        binding = DialogCompletedQuestContentBinding.inflate(context.layoutInflater, this)
    }

    fun setQuestContent(questContent: QuestContent) {
        binding.titleTextView.text = questContent.text
        binding.notesTextView.text = questContent.completion
        DataBindingUtils.loadImage(binding.imageView, "quest_" + questContent.key)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater

        if (questContent.drop != null && questContent.drop?.items != null) {
            questContent.drop?.items
                    ?.filterNot { it.isOnlyOwner }
                    ?.forEach { addRewardsRow(inflater, it, binding.rewardsList) }

            var hasOwnerRewards = false
            for (item in questContent.drop?.items ?: emptyList<QuestDropItem>()) {
                if (item.isOnlyOwner) {
                    addRewardsRow(inflater, item, binding.ownerRewardsList)
                    hasOwnerRewards = true
                }
            }
            if (!hasOwnerRewards) {
                binding.ownerRewardsTitle.visibility = View.GONE
                binding.ownerRewardsList.visibility = View.GONE
            }

            if (questContent.drop?.exp ?: 0 > 0) {
                val view = inflater?.inflate(R.layout.row_quest_reward_imageview, binding.rewardsList, false) as? ViewGroup
                val imageView = view?.findViewById<ImageView>(R.id.imageView)
                imageView?.scaleType = ImageView.ScaleType.CENTER
                imageView?.setImageBitmap(PiloterrIconsHelper.imageOfExperienceReward())
                val titleTextView = view?.findViewById<TextView>(R.id.titleTextView)
                titleTextView?.text = context.getString(R.string.experience_reward, questContent.drop?.exp)
                binding.rewardsList.addView(view)
            }

            if (questContent.drop?.gp ?: 0 > 0) {
                val view = inflater?.inflate(R.layout.row_quest_reward_imageview, binding.rewardsList, false) as? ViewGroup
                val imageView = view?.findViewById<ImageView>(R.id.imageView)
                imageView?.scaleType = ImageView.ScaleType.CENTER
                imageView?.setImageBitmap(PiloterrIconsHelper.imageOfGoldReward())
                val titleTextView = view?.findViewById<TextView>(R.id.titleTextView)
                titleTextView?.text = context.getString(R.string.gold_reward, questContent.drop?.gp)
                binding.rewardsList.addView(view)
            }
        }
    }

    private fun addRewardsRow(inflater: LayoutInflater?, item: QuestDropItem, containerView: ViewGroup?) {
        val view = inflater?.inflate(R.layout.row_quest_reward, containerView, false) as? ViewGroup
        val imageView = view?.findViewById(R.id.imageView) as? SimpleDraweeView
        val titleTextView = view?.findViewById(R.id.titleTextView) as? TextView
        DataBindingUtils.loadImage(imageView, item.imageName)
        if (item.count > 1) {
            titleTextView?.text = context.getString(R.string.quest_reward_count, item.text, item.count)
        } else {
            titleTextView?.text = item.text
        }
        containerView?.addView(view)
    }
}