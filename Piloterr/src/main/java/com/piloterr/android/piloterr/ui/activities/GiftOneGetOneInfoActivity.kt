package com.piloterr.android.piloterr.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.databinding.ActivityGift1get1InfoBinding
import com.piloterr.android.piloterr.extensions.addCancelButton
import com.piloterr.android.piloterr.ui.views.dialogs.PiloterrAlertDialog

class GiftOneGetOneInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityGift1get1InfoBinding

    override fun getLayoutResId(): Int {
        return R.layout.activity_gift1get1_info
    }

    override fun injectActivity(component: UserComponent?) {

    }

    override fun getContentView(): View {
        binding = ActivityGift1get1InfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.giftButton.setOnClickListener {
            showGiftSubscriptionDialog()
        }
    }

    private fun showGiftSubscriptionDialog() {
        val chooseRecipientDialogView = layoutInflater.inflate(R.layout.dialog_choose_message_recipient, null)

        val alert = PiloterrAlertDialog(this)
        alert.setTitle(getString(R.string.gift_title))
        alert.addButton(getString(R.string.action_continue), true) { _, _ ->
            val usernameEditText = chooseRecipientDialogView?.findViewById<View>(R.id.uuidEditText) as? EditText
            val intent = Intent(this, GiftSubscriptionActivity::class.java).apply {
                putExtra("username", usernameEditText?.text.toString())
            }
            startActivity(intent)
            finish()
        }
        alert.addCancelButton { _, _ ->
        }
        alert.setAdditionalContentView(chooseRecipientDialogView)
        alert.show()
    }
}