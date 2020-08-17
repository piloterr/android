package com.piloterr.android.piloterr.ui.fragments.social

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.ChallengeRepository
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.databinding.FragmentGuildsOverviewBinding
import com.piloterr.android.piloterr.helpers.MainNavigationController
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.social.Group
import com.piloterr.android.piloterr.ui.fragments.BaseMainFragment
import com.piloterr.android.piloterr.ui.helpers.resetViews
import io.reactivex.functions.Consumer
import io.realm.RealmResults
import java.util.*
import javax.inject.Inject

class GuildsOverviewFragment : BaseMainFragment(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    private var binding: FragmentGuildsOverviewBinding? = null
    @Inject
    lateinit var socialRepository: SocialRepository
    @Inject
    lateinit var challengeRepository: ChallengeRepository

    private var guilds: List<Group>? = null
    private var guildIDs: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.fetchGuilds()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentGuildsOverviewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetViews()

        binding?.refreshLayout?.setOnRefreshListener(this)
        binding?.publicGuildsButton?.setOnClickListener {
            MainNavigationController.navigate(GuildsOverviewFragmentDirections.openPublicGuilds())
        }
        compositeSubscription.add(socialRepository.getUserGroups().subscribe(Consumer { this.setGuilds(it) }, RxErrorHandler.handleEmptyError()))
    }
    override fun onDestroy() {
        socialRepository.close()
        challengeRepository.close()
        super.onDestroy()
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    override fun onRefresh() {
        binding?.refreshLayout?.isRefreshing = true
        fetchGuilds()
    }

    private fun fetchGuilds() {
        compositeSubscription.add(this.socialRepository.retrieveGroups("guilds")
                .subscribe(Consumer {
                    binding?.refreshLayout?.isRefreshing = false
                }, RxErrorHandler.handleEmptyError()))
    }

    private fun setGuilds(guilds: RealmResults<Group>) {
        this.guilds = guilds
        if (binding?.myGuildsListview == null) {
            return
        }
        this.guildIDs = ArrayList()
        binding?.myGuildsListview?.removeAllViewsInLayout()
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        for (guild in guilds) {
            val entry = inflater?.inflate(R.layout.plain_list_item, binding?.myGuildsListview, false) as? TextView
            entry?.text = guild.name
            entry?.setOnClickListener {
                MainNavigationController.navigate(GuildsOverviewFragmentDirections.openGuildDetail(guild.id))
            }
            binding?.myGuildsListview?.addView(entry)
            this.guildIDs?.add(guild.id)
        }
    }
}
