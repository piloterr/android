package com.piloterr.android.piloterr.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.events.ConsumablePurchasedEvent
import com.piloterr.android.piloterr.extensions.addOkButton
import com.piloterr.android.piloterr.helpers.AppConfigManager
import com.piloterr.android.piloterr.helpers.PurchaseHandler
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.proxy.CrashlyticsProxy
import com.piloterr.android.piloterr.ui.fragments.purchases.GiftBalanceGemsFragment
import com.piloterr.android.piloterr.ui.fragments.purchases.GiftPurchaseGemsFragment
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.views.dialogs.PiloterrAlertDialog
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class GiftGemsActivity : BaseActivity() {

    @Inject
    lateinit var crashlyticsProxy: CrashlyticsProxy
    @Inject
    lateinit var socialRepository: SocialRepository
    @Inject
    lateinit var appConfigManager: AppConfigManager

    private var purchaseHandler: PurchaseHandler? = null

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    internal val tabLayout: TabLayout by bindView(R.id.tab_layout)
    internal val viewPager: ViewPager by bindView(R.id.viewPager)

    private var giftedUsername: String? = null
    private var giftedUserID: String? = null

    private var purchaseFragment: GiftPurchaseGemsFragment? = null
    private var balanceFragment: GiftBalanceGemsFragment? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_gift_gems
    }

    override fun injectActivity(component: UserComponent?) {
        component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.gift_gems)
        setSupportActionBar(toolbar)

        purchaseHandler = PurchaseHandler(this, crashlyticsProxy)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        giftedUserID = intent.getStringExtra("userID")
        giftedUsername = intent.getStringExtra("username")
        if (giftedUserID == null && giftedUsername == null) {
            giftedUserID = navArgs<GiftGemsActivityArgs>().value.userID
            giftedUsername = navArgs<GiftGemsActivityArgs>().value.username
        }

        setViewPagerAdapter()

        compositeSubscription.add(socialRepository.getMember(giftedUsername ?: giftedUserID).firstElement().subscribe(Consumer {
            giftedUserID = it.id
            giftedUsername = it.username
            purchaseFragment?.giftedMember = it
            balanceFragment?.giftedMember = it
        }, RxErrorHandler.handleEmptyError()))
    }

    override fun onStart() {
        super.onStart()
        purchaseHandler?.startListening()
    }

    override fun onStop() {
        purchaseHandler?.stopListening()
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        purchaseHandler?.onResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setViewPagerAdapter() {
        val fragmentManager = supportFragmentManager

        viewPager.adapter = object : FragmentPagerAdapter(fragmentManager) {

            override fun getItem(position: Int): Fragment {
                return if (position == 0) {
                     val fragment = GiftPurchaseGemsFragment()
                    fragment.setPurchaseHandler(purchaseHandler)
                    fragment.setupCheckout()
                    purchaseFragment = fragment
                    fragment
                } else {
                     val fragment = GiftBalanceGemsFragment()
                    fragment.onCompleted = {
                        displayConfirmationDialog()
                    }
                    balanceFragment = fragment
                    fragment
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> getString(R.string.purchase)
                    1 -> getString(R.string.from_balance)
                    else -> ""
                }
            }
        }

        tabLayout.setupWithViewPager(viewPager)
    }

    @Subscribe
    fun onConsumablePurchased(event: ConsumablePurchasedEvent) {
        purchaseHandler?.consumePurchase(event.purchase)
        runOnUiThread {
            displayConfirmationDialog()
        }
    }

    private fun displayConfirmationDialog() {
        val message = getString(R.string.gift_confirmation_text_gems, giftedUsername)
        val alert = PiloterrAlertDialog(this)
        alert.setTitle(R.string.gift_confirmation_title)
        alert.setMessage(message)
        alert.addOkButton { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        alert.enqueue()
    }
}