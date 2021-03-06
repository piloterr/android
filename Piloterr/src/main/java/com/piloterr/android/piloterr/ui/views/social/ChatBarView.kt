package com.piloterr.android.piloterr.ui.views.social

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.extensions.OnChangeTextWatcher
import com.piloterr.android.piloterr.extensions.getThemeColor
import com.piloterr.android.piloterr.helpers.AppConfigManager
import com.piloterr.android.piloterr.models.social.ChatMessage
import com.piloterr.android.piloterr.ui.helpers.AutocompleteAdapter
import com.piloterr.android.piloterr.ui.helpers.AutocompleteTokenizer
import com.piloterr.android.piloterr.ui.helpers.bindView
import javax.inject.Inject


class ChatBarView : LinearLayout {

    @Inject
    lateinit var socialRepository: SocialRepository

    @Inject
    lateinit var appConfigManager: AppConfigManager

    private val sendButton: ImageButton by bindView(R.id.sendButton)
    private val chatEditText: MultiAutoCompleteTextView by bindView(R.id.chatEditText)
    private val textIndicator: TextView by bindView(R.id.text_indicator)
    private val indicatorSpacing: View by bindView(R.id.indicator_spacing)

    var chatMessages: List<ChatMessage>
        get() = autocompleteAdapter?.chatMessages ?: listOf()
        set(value) {
            autocompleteAdapter?.chatMessages = value
        }

    internal var maxChatLength = 3000L

    var sendAction: ((String) -> Unit)? = null
    var autocompleteContext: String = ""
        set(value) {
            field = value
            autocompleteAdapter?.autocompleteContext = value
        }
    var groupID: String? = null
        set(value) {
            field = value
            autocompleteAdapter?.groupID = value
        }

    var message: String?
    get() = chatEditText.text.toString()
    set(value) = chatEditText.setText(value)

    constructor(context: Context) : super(context) {
        setupView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupView(context)
    }

    private var autocompleteAdapter: AutocompleteAdapter? = null

    private fun setupView(context: Context) {
        orientation = LinearLayout.VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        inflater?.inflate(R.layout.tavern_chat_new_entry_item, this)
        this.setBackgroundResource(R.color.white)

        PiloterrBaseApplication.userComponent?.inject(this)

        chatEditText.addTextChangedListener(OnChangeTextWatcher { _, _, _, _ ->
                setSendButtonEnabled(chatEditText.text.isNotEmpty() && chatEditText.text.length <= maxChatLength)
                updateTextIndicator(chatEditText.text.toString())
        })

        sendButton.setOnClickListener { sendButtonPressed() }

        autocompleteAdapter = AutocompleteAdapter(context, socialRepository, autocompleteContext, groupID, appConfigManager.enableUsernameAutocomplete())
        chatEditText.setAdapter(autocompleteAdapter)
        chatEditText.threshold = 2

        chatEditText.setTokenizer(AutocompleteTokenizer(listOf('@', ':')))
    }

    private fun updateTextIndicator(text: String) {
        if (chatEditText.lineCount >= 3) {
            textIndicator.visibility = View.VISIBLE
            indicatorSpacing.visibility = View.VISIBLE
            textIndicator.text = "${text.length}/$maxChatLength"
            val color = when {
                text.length > maxChatLength -> R.color.red_50
                text.length > (maxChatLength * 0.95) -> R.color.yellow_5
                else -> R.color.gray_400
            }
            textIndicator.setTextColor(ContextCompat.getColor(context, color))
        } else {
            textIndicator.visibility = View.GONE
            indicatorSpacing.visibility = View.GONE
        }
    }

    private fun setSendButtonEnabled(enabled: Boolean) {
        val tintColor: Int = if (enabled) {
            context.getThemeColor(R.attr.colorAccent)
        } else {
            ContextCompat.getColor(context, R.color.gray_400)
        }
        sendButton.setColorFilter(tintColor)
        sendButton.isEnabled = enabled
    }

    private fun sendButtonPressed() {
        val chatText = message ?: ""
        if (chatText.isNotEmpty()) {
            chatEditText.text = null
            sendAction?.invoke(chatText)
        }
    }
}
