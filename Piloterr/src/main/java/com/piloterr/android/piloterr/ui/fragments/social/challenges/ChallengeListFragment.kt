package com.piloterr.android.piloterr.ui.fragments.social.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.ChallengeRepository
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.data.UserRepository
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.helpers.MainNavigationController
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.social.Challenge
import com.piloterr.android.piloterr.models.social.Group
import com.piloterr.android.piloterr.modules.AppModule
import com.piloterr.android.piloterr.ui.adapter.social.ChallengesListViewAdapter
import com.piloterr.android.piloterr.ui.fragments.BaseFragment
import com.piloterr.android.piloterr.ui.helpers.RecyclerViewEmptySupport
import com.piloterr.android.piloterr.ui.helpers.SafeDefaultItemAnimator
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.helpers.resetViews
import com.piloterr.android.piloterr.utils.Action1
import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.combineLatest
import io.realm.RealmResults
import javax.inject.Inject
import javax.inject.Named


class ChallengeListFragment : BaseFragment(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var challengeRepository: ChallengeRepository
    @Inject
    lateinit var socialRepository: SocialRepository
    @Inject
    lateinit var userRepository: UserRepository
    @field:[Inject Named(AppModule.NAMED_USER_ID)]
    lateinit var userId: String

    private val swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? by bindView(R.id.refreshLayout)
    private val recyclerView: RecyclerViewEmptySupport? by bindView(R.id.recyclerView)
    private val emptyView: View? by bindView(R.id.emptyView)

    private var challengeAdapter: ChallengesListViewAdapter? = null
    private var viewUserChallengesOnly: Boolean = false

    private var nextPageToLoad = 0
    private var loadedAllData = false

    private var challenges: RealmResults<Challenge>? = null
    private var filterGroups: MutableList<Group>? = null

    private var filterOptions: ChallengeFilterOptions? = null

    fun setViewUserChallengesOnly(only: Boolean) {
        this.viewUserChallengesOnly = only
    }

    override fun onDestroy() {
        challengeRepository.close()
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return container?.inflate(R.layout.fragment_challengeslist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetViews()

        challengeAdapter = ChallengesListViewAdapter(null, true, viewUserChallengesOnly, userId)
        challengeAdapter?.getOpenDetailFragmentFlowable()?.subscribe(Consumer { openDetailFragment(it) }, RxErrorHandler.handleEmptyError())
                ?.let { compositeSubscription.add(it) }

        swipeRefreshLayout?.setOnRefreshListener(this)

        recyclerView?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.activity)
        recyclerView?.adapter = challengeAdapter
        if (!viewUserChallengesOnly) {
            this.recyclerView?.setBackgroundResource(R.color.white)
        }

        compositeSubscription.add(socialRepository.getGroup(Group.TAVERN_ID).combineLatest(socialRepository.getUserGroups()).subscribe(Consumer {
            this.filterGroups = mutableListOf()
            filterGroups?.add(it.first)
            filterGroups?.addAll(it.second)
        }, RxErrorHandler.handleEmptyError()))

        recyclerView?.setEmptyView(emptyView)
        recyclerView?.itemAnimator = SafeDefaultItemAnimator()

        challengeAdapter?.updateUnfilteredData(challenges)
        loadLocalChallenges()

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    retrieveChallengesPage()
                }
            }
        })
    }

    private fun openDetailFragment(challengeID: String) {
        MainNavigationController.navigate(ChallengesOverviewFragmentDirections.openChallengeDetail(challengeID))
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    override fun onRefresh() {
        nextPageToLoad = 0
        loadedAllData = false
        retrieveChallengesPage(true)
    }

    private fun setRefreshing(state: Boolean) {
        swipeRefreshLayout?.isRefreshing = state
    }

    private fun loadLocalChallenges() {
        val observable: Flowable<RealmResults<Challenge>> = if (viewUserChallengesOnly) {
            challengeRepository.getUserChallenges()
        } else {
            challengeRepository.getChallenges()
        }

        compositeSubscription.add(observable.firstElement().subscribe(Consumer { challenges ->
            if (challenges.size == 0) {
                retrieveChallengesPage()
            }
            this.challenges = challenges
            challengeAdapter?.updateUnfilteredData(challenges)
        }, RxErrorHandler.handleEmptyError()))
    }

    internal fun retrieveChallengesPage(forced: Boolean = false) {
        if ((!forced && swipeRefreshLayout?.isRefreshing == true) || loadedAllData) {
            return
        }
        setRefreshing(true)
        compositeSubscription.add(challengeRepository.retrieveChallenges(nextPageToLoad, viewUserChallengesOnly).doOnComplete {
            setRefreshing(false)
        } .subscribe(Consumer {
            if (it.size < 10) {
                loadedAllData = true
            }
            nextPageToLoad += 1
        }, RxErrorHandler.handleEmptyError()))
    }

    internal fun showFilterDialog() {
        activity?.let {
            ChallengeFilterDialogHolder.showDialog(it,
                    filterGroups ?: emptyList(),
                    filterOptions, object : Action1<ChallengeFilterOptions> {
                override fun call(t: ChallengeFilterOptions) {
                    changeFilter(t)
                }
            })
        }
    }

    private fun changeFilter(challengeFilterOptions: ChallengeFilterOptions) {
        filterOptions = challengeFilterOptions
        challengeAdapter?.filter(challengeFilterOptions)
    }
}
