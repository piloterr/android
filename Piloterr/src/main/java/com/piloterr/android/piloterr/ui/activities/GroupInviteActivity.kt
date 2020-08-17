package com.piloterr.android.piloterr.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.data.UserRepository
import com.piloterr.android.piloterr.extensions.runDelayed
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.user.User
import com.piloterr.android.piloterr.modules.AppModule
import com.piloterr.android.piloterr.ui.fragments.social.party.PartyInviteFragment
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.helpers.dismissKeyboard
import com.piloterr.android.piloterr.ui.views.PiloterrSnackbar
import com.piloterr.android.piloterr.ui.views.PiloterrSnackbar.Companion.showSnackbar
import io.reactivex.functions.Consumer
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named



class GroupInviteActivity : BaseActivity() {

    @field:[Inject Named(AppModule.NAMED_USER_ID)]
    lateinit var userId: String
    @Inject
    lateinit var socialRepository: SocialRepository
    @Inject
    lateinit var userRepository: UserRepository

    internal val tabLayout: TabLayout by bindView(R.id.tab_layout)
    internal val viewPager: ViewPager by bindView(R.id.viewPager)
    private val snackbarView: ViewGroup by bindView(R.id.snackbar_view)

    internal var fragments: MutableList<PartyInviteFragment> = ArrayList()
    private var userIdToInvite: String? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_party_invite
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar(findViewById(R.id.toolbar))
        viewPager.currentItem = 0

        supportActionBar?.title = null

        setViewPagerAdapter()
    }

    override fun injectActivity(component: UserComponent?) {
        component?.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_party_invite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_send_invites -> {
                setResult(Activity.RESULT_OK, createResultIntent())
                dismissKeyboard()
                if (fragments.size > viewPager.currentItem && fragments[viewPager.currentItem].values.isNotEmpty()) {
                    showSnackbar(snackbarView, "Invite Sent!", PiloterrSnackbar.SnackbarDisplayType.SUCCESS)
                    runDelayed(1, TimeUnit.SECONDS, this::finish)
                } else {
                    finish()
                }
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createResultIntent(): Intent {
        val intent = Intent()
        if (fragments.size == 0) return intent
        val fragment = fragments[viewPager.currentItem]
        intent.putExtra(EMAILS_KEY, fragments[1].values)
        intent.putExtra(USER_IDS_KEY, fragments[0].values)
        return intent
    }

    private fun setViewPagerAdapter() {
        val fragmentManager = supportFragmentManager

        viewPager.adapter = object : FragmentPagerAdapter(fragmentManager) {

            override fun getItem(position: Int): Fragment {
                val fragment = PartyInviteFragment()
                fragment.isEmailInvite = position == 1
                if (fragments.size > position) {
                    fragments[position] = fragment
                } else {
                    fragments.add(fragment)
                }

                return fragment
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> getString(R.string.invite_existing_users)
                    1 -> getString(R.string.by_email)
                    else -> ""
                }
            }
        }

        tabLayout.setupWithViewPager(viewPager)
    }

    private fun handleUserReceived(user: User) {
        if (this.userIdToInvite == null) {
            return
        }

        val inviteData = HashMap<String, Any>()
        val invites = ArrayList<String>()
        userIdToInvite?.let {
            invites.add(it)
        }
        inviteData["uuids"] = invites

        compositeSubscription.add(this.socialRepository.inviteToGroup(user.party?.id ?: "", inviteData)
                .subscribe(Consumer { }, RxErrorHandler.handleEmptyError()))
    }

    companion object {

        const val RESULT_SEND_INVITES = 100
        const val USER_IDS_KEY = "userIDs"
        const val IS_EMAIL_KEY = "isEmail"
        const val EMAILS_KEY = "emails"
    }
}
