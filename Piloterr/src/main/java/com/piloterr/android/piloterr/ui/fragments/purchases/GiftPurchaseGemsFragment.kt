package com.piloterr.android.piloterr.ui.fragments.purchases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.piloterr.android.piloterr.PiloterrPurchaseVerifier
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.helpers.PurchaseHandler
import com.piloterr.android.piloterr.helpers.PurchaseTypes
import com.piloterr.android.piloterr.models.members.Member
import com.piloterr.android.piloterr.proxy.CrashlyticsProxy
import com.piloterr.android.piloterr.ui.AvatarView
import com.piloterr.android.piloterr.ui.GemPurchaseOptionsView
import com.piloterr.android.piloterr.ui.fragments.BaseFragment
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.views.social.UsernameLabel
import javax.inject.Inject

class GiftPurchaseGemsFragment : BaseFragment() {

    @Inject
    lateinit var crashlyticsProxy: CrashlyticsProxy
    @Inject
    lateinit var socialRepository: SocialRepository

    private val avatarView: AvatarView by bindView(R.id.avatar_view)
    private val displayNameTextView: UsernameLabel by bindView(R.id.display_name_textview)
    private val usernameTextView: TextView by bindView(R.id.username_textview)
    private val gems4View: GemPurchaseOptionsView? by bindView(R.id.gems_4_view)
    private val gems21View: GemPurchaseOptionsView? by bindView(R.id.gems_21_view)
    private val gems42View: GemPurchaseOptionsView? by bindView(R.id.gems_42_view)
    private val gems84View: GemPurchaseOptionsView? by bindView(R.id.gems_84_view)

    var giftedMember: Member? = null
    set(value) {
        field = value
        field?.let {
            avatarView.setAvatar(it)
            displayNameTextView.username = it.profile?.name
            displayNameTextView.tier = it.contributor?.level ?: 0
            usernameTextView.text = "@${it.username}"
        }
    }

    private var purchaseHandler: PurchaseHandler? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return container?.inflate(R.layout.fragment_gift_gem_purchase)
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gems4View?.setOnPurchaseClickListener(View.OnClickListener { purchaseGems(PurchaseTypes.Purchase4Gems) })
        gems21View?.setOnPurchaseClickListener(View.OnClickListener { purchaseGems(PurchaseTypes.Purchase21Gems) })
        gems42View?.setOnPurchaseClickListener(View.OnClickListener { purchaseGems(PurchaseTypes.Purchase42Gems) })
        gems84View?.setOnPurchaseClickListener(View.OnClickListener { purchaseGems(PurchaseTypes.Purchase84Gems) })
    }

    fun setupCheckout() {
        purchaseHandler?.getAllGemSKUs { skus ->
            for (sku in skus) {
                updateButtonLabel(sku.id.code, sku.price)
            }
        }
    }

    fun setPurchaseHandler(handler: PurchaseHandler?) {
        this.purchaseHandler = handler
    }

    private fun updateButtonLabel(sku: String, price: String) {
        val matchingView: GemPurchaseOptionsView? = when (sku) {
            PurchaseTypes.Purchase4Gems -> gems4View
            PurchaseTypes.Purchase21Gems -> gems21View
            PurchaseTypes.Purchase42Gems -> gems42View
            PurchaseTypes.Purchase84Gems -> gems84View
            else -> return
        }
        if (matchingView != null) {
            matchingView.setPurchaseButtonText(price)
            matchingView.sku = sku
        }
    }

    private fun purchaseGems(identifier: String) {
        PiloterrPurchaseVerifier.addGift(identifier, giftedMember?.id)
        purchaseHandler?.purchaseGems(identifier)
    }
}
