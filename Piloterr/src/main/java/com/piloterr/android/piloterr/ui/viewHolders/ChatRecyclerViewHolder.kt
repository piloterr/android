package com.piloterr.android.piloterr.ui.viewHolders

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.dpToPx
import com.piloterr.android.piloterr.extensions.getAgoString
import com.piloterr.android.piloterr.extensions.setScaledPadding
import com.piloterr.android.piloterr.models.social.ChatMessage
import com.piloterr.android.piloterr.models.user.User
import com.piloterr.android.piloterr.ui.AvatarView
import com.piloterr.android.piloterr.ui.helpers.DataBindingUtils
import com.piloterr.android.piloterr.ui.helpers.MarkdownParser
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.views.PiloterrEmojiTextView
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import com.piloterr.android.piloterr.ui.views.social.UsernameLabel
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChatRecyclerViewHolder(itemView: View, private var userId: String, private val isTavern: Boolean) : RecyclerView.ViewHolder(itemView) {

    private val messageWrapper: ViewGroup by bindView(R.id.message_wrapper)
    private val avatarView: AvatarView by bindView(R.id.avatar_view)
    private val userLabel: UsernameLabel by bindView(R.id.user_label)
    private val messageText: PiloterrEmojiTextView by bindView(R.id.message_text)
    private val sublineTextView: TextView by bindView(R.id.subline_textview)
    private val likeBackground: LinearLayout by bindView(R.id.like_background_layout)
    private val tvLikes: TextView by bindView(R.id.tvLikes)
    private val buttonsWrapper: ViewGroup by bindView(R.id.buttons_wrapper)
    private val replyButton: Button by bindView(R.id.reply_button)
    private val copyButton: Button by bindView(R.id.copy_button)
    private val reportButton: Button by bindView(R.id.report_button)
    private val deleteButton: Button by bindView(R.id.delete_button)
    private val modView: TextView by bindView(R.id.mod_view)

    val context: Context = itemView.context
    val res: Resources = itemView.resources
    private var chatMessage: ChatMessage? = null
    private var user: User? = null

    var onShouldExpand: (() -> Unit)? = null
    var onLikeMessage: ((ChatMessage) -> Unit)? = null
    var onOpenProfile: ((String) -> Unit)? = null
    var onReply: ((String) -> Unit)? = null
    var onCopyMessage: ((ChatMessage) -> Unit)? = null
    var onFlagMessage: ((ChatMessage) -> Unit)? = null
    var onDeleteMessage: ((ChatMessage) -> Unit)? = null

    init {
        itemView.setOnClickListener {
            onShouldExpand?.invoke()
        }
        tvLikes.setOnClickListener { chatMessage?.let { onLikeMessage?.invoke(it) } }
        messageText.setOnClickListener { onShouldExpand?.invoke() }
        messageText.movementMethod = LinkMovementMethod.getInstance()
        userLabel.setOnClickListener { chatMessage?.uuid?.let { onOpenProfile?.invoke(it) } }
        avatarView.setOnClickListener { chatMessage?.uuid?.let { onOpenProfile?.invoke(it) } }
        replyButton.setOnClickListener {
            if (chatMessage?.username != null) {
                chatMessage?.username?.let { onReply?.invoke(it) }
            } else {
                chatMessage?.user?.let { onReply?.invoke(it) }
            }
        }
        replyButton.setCompoundDrawablesWithIntrinsicBounds(BitmapDrawable(res, PiloterrIconsHelper.imageOfChatReplyIcon()),
                null, null, null)
        copyButton.setOnClickListener { chatMessage?.let { onCopyMessage?.invoke(it) } }
        copyButton.setCompoundDrawablesWithIntrinsicBounds(BitmapDrawable(res, PiloterrIconsHelper.imageOfChatCopyIcon()),
                null, null, null)
        reportButton.setOnClickListener { chatMessage?.let { onFlagMessage?.invoke(it) } }
        reportButton.setCompoundDrawablesWithIntrinsicBounds(BitmapDrawable(res, PiloterrIconsHelper.imageOfChatReportIcon()),
                null, null, null)
        deleteButton.setOnClickListener { chatMessage?.let { onDeleteMessage?.invoke(it) } }
        deleteButton.setCompoundDrawablesWithIntrinsicBounds(BitmapDrawable(res, PiloterrIconsHelper.imageOfChatDeleteIcon()),
                null, null, null)
    }

    fun bind(msg: ChatMessage, uuid: String, user: User?, isExpanded: Boolean) {
        chatMessage = msg
        this.user = user
        userId = uuid

        setLikeProperties()

        val wasSent = messageWasSent()

        val name = user?.profile?.name
        if (wasSent) {
            userLabel.isNPC = user?.backer?.npc != null
            userLabel.tier = user?.contributor?.level ?: 0
            userLabel.username = name
            if (user?.username != null) {
                @SuppressLint("SetTextI18n")
                sublineTextView.text = "${user.formattedUsername} ∙ ${msg.timestamp?.getAgoString(res)}"
            } else {
                sublineTextView.text = msg.timestamp?.getAgoString(res)
            }
        } else {
            userLabel.isNPC = msg.backer?.npc != null
            userLabel.tier = msg.contributor?.level ?: 0
            userLabel.username = msg.user
            if (msg.username != null) {
                @SuppressLint("SetTextI18n")
                sublineTextView.text = "${msg.formattedUsername} ∙ ${msg.timestamp?.getAgoString(res)}"
            } else {
                sublineTextView.text = msg.timestamp?.getAgoString(res)
            }
        }
        when {
            userLabel.tier == 8 -> {
                modView.visibility = View.VISIBLE
                modView.text = context.getString(R.string.moderator)
                modView.background = ContextCompat.getDrawable(context, R.drawable.pill_bg_blue)
                modView.setScaledPadding(context, 12, 4, 12, 4)
            }
            userLabel.tier == 9 -> {
                modView.visibility = View.VISIBLE
                modView.text = context.getString(R.string.staff)
                modView.background = ContextCompat.getDrawable(context, R.drawable.pill_bg_purple_300)
                modView.setScaledPadding(context, 12, 4, 12, 4)
            }
            else -> modView.visibility = View.GONE
        }

        if (wasSent) {
            avatarView.visibility = View.GONE
            itemView.setPadding(64.dpToPx(context), itemView.paddingTop, itemView.paddingRight, itemView.paddingBottom)
        } else {
            val displayMetrics = res.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            if (dpWidth > 350) {
                avatarView.visibility = View.VISIBLE
                msg.userStyles?.let {
                    avatarView.setAvatar(it)
                }
            } else {
                avatarView.visibility = View.GONE
            }
            itemView.setPadding(16.dpToPx(context), itemView.paddingTop, itemView.paddingRight, itemView.paddingBottom)
        }

        messageText.text = chatMessage?.parsedText
        if (msg.parsedText == null) {
            messageText.text = chatMessage?.text
            Maybe.just(chatMessage?.text ?: "")
                    .map { MarkdownParser.parseMarkdown(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ parsedText ->
                        chatMessage?.parsedText = parsedText
                        messageText.text = chatMessage?.parsedText
                    }, { it.printStackTrace() })
        }

        val username = user?.formattedUsername
        messageWrapper.background = if ((name != null && msg.text?.contains("@$name") == true) || (username != null && msg.text?.contains(username) == true)) {
            ContextCompat.getDrawable(context, R.drawable.layout_rounded_bg_brand_700)
        } else {
            ContextCompat.getDrawable(context, R.drawable.layout_rounded_bg)
        }
        messageWrapper.setScaledPadding(context, 8, 8, 8, 8)

        if (isExpanded) {
            buttonsWrapper.visibility = View.VISIBLE
            deleteButton.visibility = if (shouldShowDelete()) View.VISIBLE else View.GONE
            replyButton.visibility = if (chatMessage?.isInboxMessage == true) View.GONE else View.VISIBLE
        } else {
            buttonsWrapper.visibility = View.GONE
        }
    }

    private fun messageWasSent(): Boolean {
        return chatMessage?.sent == true || chatMessage?.uuid == userId
    }

    private fun setLikeProperties() {
        likeBackground.visibility = if (isTavern) View.VISIBLE else View.INVISIBLE
        @SuppressLint("SetTextI18n")
        tvLikes.text = "+" + chatMessage?.likeCount

        val backgroundColorRes: Int
        val foregroundColorRes: Int

        if (chatMessage?.likeCount != 0) {
            if (chatMessage?.userLikesMessage(userId) == true) {
                backgroundColorRes = R.color.tavern_userliked_background
                foregroundColorRes = R.color.tavern_userliked_foreground
            } else {
                backgroundColorRes = R.color.tavern_somelikes_background
                foregroundColorRes = R.color.tavern_somelikes_foreground
            }
        } else {
            backgroundColorRes = R.color.tavern_nolikes_background
            foregroundColorRes = R.color.tavern_nolikes_foreground
        }

        DataBindingUtils.setRoundedBackground(likeBackground, ContextCompat.getColor(context, backgroundColorRes))
        tvLikes.setTextColor(ContextCompat.getColor(context, foregroundColorRes))
    }

    private fun shouldShowDelete(): Boolean {
        return chatMessage?.isSystemMessage != true && (chatMessage?.uuid == userId || user?.contributor?.admin == true || chatMessage?.isInboxMessage == true)
    }
}