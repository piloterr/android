package com.piloterr.android.piloterr.ui.viewHolders.GroupMemberViewHolder

import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.models.members.Member
import com.piloterr.android.piloterr.models.user.Stats
import com.piloterr.android.piloterr.ui.AvatarView
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import com.piloterr.android.piloterr.ui.views.PiloterrProgressBar
import com.piloterr.android.piloterr.ui.views.social.UsernameLabel


class GroupMemberViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {

    private var currentUserID: String? = null
    private var leaderID: String? = null
    private val avatarView: AvatarView by bindView(R.id.avatarView)
    private val displayNameTextView: UsernameLabel by bindView(R.id.display_name_textview)
    private val sublineTextView: TextView by bindView(R.id.subline_textview)
    private val buffIconView: ImageView by bindView(R.id.buff_icon_view)
    private val classIconView: ImageView by bindView(R.id.class_icon_view)
    private val healthBar: PiloterrProgressBar by bindView(R.id.health_bar)
    private val experienceBar: PiloterrProgressBar by bindView(R.id.experience_bar)
    private val manaBar: PiloterrProgressBar by bindView(R.id.mana_bar)
    private val healthTextView: TextView by bindView(R.id.health_textview)
    private val experienceTextView: TextView by bindView(R.id.experience_textview)
    private val manaTextView: TextView by bindView(R.id.mana_textview)
    private val moreButton: ImageButton by bindView(R.id.more_button)
    //private val leaderTextView: TextView by bindView(R.id.leader_textview)

    var onClickEvent: (() -> Unit)? = null
    var sendMessageEvent: (() -> Unit)? = null
    var removeMemberEvent: (() -> Unit)? = null
    var transferOwnershipEvent: (() -> Unit)? = null

    init {
        buffIconView.setImageBitmap(PiloterrIconsHelper.imageOfBuffIcon())
        itemView.setOnClickListener { onClickEvent?.invoke() }
        moreButton.setOnClickListener { showOptionsPopup() }
    }

    private fun showOptionsPopup() {
        val popup = PopupMenu(itemView.context, moreButton)
        popup.setOnMenuItemClickListener(this)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.party_member_menu, popup.menu)
        popup.menu.findItem(R.id.transfer_ownership).isVisible = currentUserID == leaderID
        popup.menu.findItem(R.id.remove).isVisible = currentUserID == leaderID
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.send_message -> { sendMessageEvent?.invoke() }
            R.id.transfer_ownership -> { transferOwnershipEvent?.invoke() }
            R.id.remove -> { removeMemberEvent?.invoke() }
        }
        return true
    }

    fun bind(user: Member, leaderID: String?, userID: String?) {
        avatarView.setAvatar(user)
        this.leaderID = leaderID
        this.currentUserID = userID

        if (user.id == userID) {
            moreButton.visibility = View.GONE
        } else {
            moreButton.visibility = View.VISIBLE
        }

        user.stats?.let {
            healthBar.set(it.hp ?: 0.0, it.maxHealth?.toDouble() ?: 50.0)
            healthTextView.text = "${it.hp?.toInt()} / ${it.maxHealth}"
            experienceBar.set(it.exp ?: 0.0, it.toNextLevel?.toDouble() ?: 0.0)
            experienceTextView.text = "${it.exp?.toInt()} / ${it.toNextLevel}"
            manaBar.set(it.mp ?: 0.0, it.maxMP?.toDouble() ?: 0.0)
            manaTextView.text = "${it.mp?.toInt()} / ${it.maxMP}"
        }
        displayNameTextView.username = user.profile?.name
        displayNameTextView.tier = user.contributor?.level ?: 0

        if (user.hasClass()) {
            sublineTextView.text = itemView.context.getString(R.string.user_level_with_class, user.stats?.lvl, user.stats?.getTranslatedClassName(itemView.context))
        } else {
            sublineTextView.text = itemView.context.getString(R.string.user_level, user.stats?.lvl)
        }

        if (user.stats?.isBuffed == true) {
            buffIconView.visibility = View.VISIBLE
        } else {
            buffIconView.visibility = View.GONE
        }

        classIconView.visibility = View.VISIBLE
        when (user.stats?.habitClass) {
            Stats.HEALER -> {
                classIconView.setImageBitmap(PiloterrIconsHelper.imageOfHealerLightBg())
            }
            Stats.WARRIOR -> {
                classIconView.setImageBitmap(PiloterrIconsHelper.imageOfWarriorLightBg())
            }
            Stats.ROGUE -> {
                classIconView.setImageBitmap(PiloterrIconsHelper.imageOfRogueLightBg())
            }
            Stats.MAGE -> {
                classIconView.setImageBitmap(PiloterrIconsHelper.imageOfMageLightBg())
            }
            else -> {
                classIconView.visibility = View.INVISIBLE
            }
        }

        itemView.isClickable = true
        //leaderTextView.visibility = if (user.id == leaderID) View.VISIBLE else View.GONE
    }
}
