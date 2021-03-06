package com.piloterr.android.piloterr.ui.fragments.social

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.helpers.AppConfigManager
import com.piloterr.android.piloterr.helpers.MainNavigationController
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.members.Member
import com.piloterr.android.piloterr.models.user.User
import com.piloterr.android.piloterr.ui.activities.GroupFormActivity
import com.piloterr.android.piloterr.ui.fragments.BaseMainFragment
import com.piloterr.android.piloterr.ui.helpers.DataBindingUtils
import com.piloterr.android.piloterr.ui.helpers.setMarkdown
import com.piloterr.android.piloterr.ui.views.PiloterrSnackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_no_party.*
import javax.inject.Inject
import kotlin.math.roundToInt


class NoPartyFragmentFragment : BaseMainFragment() {

    @Inject
    lateinit var socialRepository: SocialRepository
    @Inject
    lateinit var configManager: AppConfigManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hidesToolbar = true
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_no_party, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout?.setOnRefreshListener { this.refresh() }

        invitations_view.acceptCall = {
            socialRepository.joinGroup(it)
                    .flatMap { userRepository.retrieveUser(false) }
                    .subscribe(Consumer {
                        fragmentManager?.popBackStack()
                        MainNavigationController.navigate(R.id.partyFragment,
                                bundleOf(Pair("partyID", user?.party?.id)))
                    }, RxErrorHandler.handleEmptyError())
        }

        invitations_view.rejectCall = {
            socialRepository.rejectGroupInvite(it).subscribe(Consumer { }, RxErrorHandler.handleEmptyError())
            invitationWrapper.visibility = View.GONE
        }

        invitations_view.setLeader = { leader ->
            compositeSubscription.add(
                    socialRepository.getMember(leader)
                            .subscribe(Consumer {
                                invitations_view.avatarView.setAvatar(it)
                                invitations_view.textView.text = getString(R.string.invitation_title,it.displayName,invitations_view.groupName)
                            }, RxErrorHandler.handleEmptyError())
            )
        }

        username_textview.setOnClickListener {
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(context?.getString(R.string.username), user?.username)
            clipboard?.setPrimaryClip(clip)
            val activity = activity
            if (activity != null) {
                PiloterrSnackbar.showSnackbar(activity.snackbarContainer, getString(R.string.username_copied), PiloterrSnackbar.SnackbarDisplayType.NORMAL)
            }
        }

        createPartyButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("groupType", "party")
            bundle.putString("leader", user?.id)
            val intent = Intent(activity, GroupFormActivity::class.java)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityForResult(intent, GroupFormActivity.GROUP_FORM_ACTIVITY)
        }

        context?.let { context ->
            DataBindingUtils.loadImage("timeTravelersShop_background_fall") {bitmap ->
                val aspectRatio = bitmap.width / bitmap.height.toFloat()
                val height = context.resources.getDimension(R.dimen.shop_height).toInt()
                val width = (height * aspectRatio).roundToInt()
                val drawable = BitmapDrawable(context.resources, Bitmap.createScaledBitmap(bitmap, width, height, false))
                drawable.tileModeX = Shader.TileMode.REPEAT
                Observable.just(drawable)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer {
                            if (no_party_background != null) {
                                no_party_background.background = it
                            }
                        }, RxErrorHandler.handleEmptyError())
            }
        }

        if (configManager.noPartyLinkPartyGuild()) {
            join_party_description_textview.setMarkdown(getString(R.string.join_party_description_guild, "[Party Wanted Guild](https://piloterr.com/groups/guild/de4c9929-6bdb-48d1-b1ff-13499fd8b822)"))
            join_party_description_textview.setOnClickListener {
                context?.let { FirebaseAnalytics.getInstance(it).logEvent("clicked_party_wanted", null) }
                MainNavigationController.navigate(R.id.guildFragment, bundleOf("groupID" to "de4c9929-6bdb-48d1-b1ff-13499fd8b822"))
            }
        }

        if ((user?.invitations?.parties?.count() ?: 0) > 0) {
            invitationWrapper.visibility = View.VISIBLE
            user?.invitations?.parties?.let { invitations_view.setInvitations(it) }
        } else {
            invitationWrapper.visibility = View.GONE
        }

        username_textview.text = user?.formattedUsername
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GroupFormActivity.GROUP_FORM_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data?.extras
                    if (bundle?.getString("groupType") == "party") {
                        socialRepository.createGroup(bundle.getString("name"),
                                bundle.getString("description"),
                                bundle.getString("leader"),
                                "party",
                                bundle.getString("privacy"),
                                bundle.getBoolean("leaderCreateChallenge"))
                                .flatMap {
                                    userRepository.retrieveUser(false)
                                }
                                .subscribe(Consumer {
                                    fragmentManager?.popBackStack()
                                    MainNavigationController.navigate(R.id.partyFragment,
                                            bundleOf(Pair("partyID", user?.party?.id)))
                                }, RxErrorHandler.handleEmptyError())
                    }
                }
            }
        }
    }

    private fun refresh() {
        compositeSubscription.add(userRepository.retrieveUser(false, forced = true)
                .filter { it.hasParty() }
                .flatMap { socialRepository.retrieveGroup("party") }
                .flatMap<List<Member>> { group1 -> socialRepository.retrieveGroupMembers(group1.id, true) }
                .doOnComplete { refreshLayout.isRefreshing = false }
                .subscribe(Consumer {  }, RxErrorHandler.handleEmptyError()))
    }

    override fun onDestroy() {
        userRepository.close()
        socialRepository.close()
        super.onDestroy()
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    companion object {

        fun newInstance(user: User?): NoPartyFragmentFragment {
            val args = Bundle()

            val fragment = NoPartyFragmentFragment()
            fragment.arguments = args
            fragment.user = user
            return fragment
        }
    }

}
