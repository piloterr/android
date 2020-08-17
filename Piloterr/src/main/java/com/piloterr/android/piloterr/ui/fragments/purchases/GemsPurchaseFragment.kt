package com.piloterr.android.piloterr.ui.fragments.purchases

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.UserRepository
import com.piloterr.android.piloterr.databinding.FragmentGemPurchaseBinding
import com.piloterr.android.piloterr.extensions.addCancelButton
import com.piloterr.android.piloterr.helpers.AppConfigManager
import com.piloterr.android.piloterr.helpers.PurchaseHandler
import com.piloterr.android.piloterr.helpers.PurchaseTypes
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.proxy.CrashlyticsProxy
import com.piloterr.android.piloterr.ui.GemPurchaseOptionsView
import com.piloterr.android.piloterr.ui.activities.GemPurchaseActivity
import com.piloterr.android.piloterr.ui.activities.GiftGemsActivity
import com.piloterr.android.piloterr.ui.activities.GiftSubscriptionActivity
import com.piloterr.android.piloterr.ui.fragments.BaseFragment
import com.piloterr.android.piloterr.ui.helpers.dismissKeyboard
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import com.piloterr.android.piloterr.ui.views.dialogs.PiloterrAlertDialog
import io.reactivex.functions.Consumer
import javax.inject.Inject

class GemsPurchaseFragment : BaseFragment(), GemPurchaseActivity.CheckoutFragment {

    private lateinit var binding: FragmentGemPurchaseBinding

    @Inject
    lateinit var crashlyticsProxy: CrashlyticsProxy
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var appConfigManager: AppConfigManager

    private var purchaseHandler: PurchaseHandler? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentGemPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gems4View.setOnPurchaseClickListener(View.OnClickListener { purchaseGems(PurchaseTypes.Purchase4Gems) })
        binding.gems21View.setOnPurchaseClickListener(View.OnClickListener { purchaseGems(PurchaseTypes.Purchase21Gems) })
        binding.gems42View.setOnPurchaseClickListener(View.OnClickListener { purchaseGems(PurchaseTypes.Purchase42Gems) })
        binding.gems84View.setOnPurchaseClickListener(View.OnClickListener { purchaseGems(PurchaseTypes.Purchase84Gems) })

        val heartDrawable = BitmapDrawable(resources, PiloterrIconsHelper.imageOfHeartLarge())
        binding.supportTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, heartDrawable)

        compositeSubscription.add(userRepository.getUser().subscribe(Consumer {
            binding.subscriptionPromo.visibility = if (it.isSubscribed) View.GONE else View.VISIBLE
        }, RxErrorHandler.handleEmptyError()))

        binding.giftGemsButton.setOnClickListener { showGiftGemsDialog() }

        binding.giftSubscriptionContainer.isVisible = appConfigManager.enableGiftOneGetOne()
        binding.giftSubscriptionContainer.setOnClickListener { showGiftSubscriptionDialog() }
    }

    override fun setupCheckout() {
        purchaseHandler?.getAllGemSKUs { skus ->
            for (sku in skus) {
                updateButtonLabel(sku.id.code, sku.price)
            }
        }
    }

    override fun setPurchaseHandler(handler: PurchaseHandler?) {
        this.purchaseHandler = handler
    }

    private fun updateButtonLabel(sku: String, price: String) {
        val matchingView: GemPurchaseOptionsView? = when (sku) {
            PurchaseTypes.Purchase4Gems -> binding.gems4View
            PurchaseTypes.Purchase21Gems -> binding.gems21View
            PurchaseTypes.Purchase42Gems -> binding.gems42View
            PurchaseTypes.Purchase84Gems -> binding.gems84View
            else -> return
        }
        if (matchingView != null) {
            matchingView.setPurchaseButtonText(price)
            matchingView.sku = sku
        }
    }

    private fun purchaseGems(identifier: String) {
        purchaseHandler?.purchaseGems(identifier)
    }

    private fun showGiftGemsDialog() {
        val chooseRecipientDialogView = this.activity?.layoutInflater?.inflate(R.layout.dialog_choose_message_recipient, null)

        this.activity?.let { thisActivity ->
            val alert = PiloterrAlertDialog(thisActivity)
            alert.setTitle(getString(R.string.gift_title))
            alert.addButton(getString(R.string.action_continue), true) { _, _ ->
                val usernameEditText = chooseRecipientDialogView?.findViewById<View>(R.id.uuidEditText) as? EditText
                val intent = Intent(thisActivity, GiftGemsActivity::class.java).apply {
                    putExtra("username", usernameEditText?.text.toString())
                }
                startActivity(intent)
            }
            alert.addCancelButton { _, _ ->
                thisActivity.dismissKeyboard()
            }
            alert.setAdditionalContentView(chooseRecipientDialogView)
            alert.show()
        }
    }

    private fun showGiftSubscriptionDialog() {
        val chooseRecipientDialogView = this.activity?.layoutInflater?.inflate(R.layout.dialog_choose_message_recipient, null)

        this.activity?.let { thisActivity ->
            val alert = PiloterrAlertDialog(thisActivity)
            alert.setTitle(getString(R.string.gift_title))
            alert.addButton(getString(R.string.action_continue), true) { _, _ ->
                val usernameEditText = chooseRecipientDialogView?.findViewById<View>(R.id.uuidEditText) as? EditText
                val intent = Intent(thisActivity, GiftSubscriptionActivity::class.java).apply {
                    putExtra("username", usernameEditText?.text.toString())
                }
                startActivity(intent)
            }
            alert.addCancelButton { _, _ ->
                thisActivity.dismissKeyboard()
            }
            alert.setAdditionalContentView(chooseRecipientDialogView)
            alert.show()
        }
    }
}
