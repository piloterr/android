package com.piloterr.android.piloterr.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.data.UserRepository
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.ui.adapter.social.PartyMemberRecyclerViewAdapter
import com.piloterr.android.piloterr.ui.helpers.bindView
import io.reactivex.functions.Consumer
import javax.inject.Inject

class SkillMemberActivity : BaseActivity() {
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    private var viewAdapter: PartyMemberRecyclerViewAdapter? = null

    @Inject
    lateinit var socialRepository: SocialRepository
    @Inject
    lateinit var userRepository: UserRepository

    override fun getLayoutResId(): Int {
        return R.layout.activity_skill_members
    }

    override fun injectActivity(component: UserComponent?) {
        component?.inject(this)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar(findViewById(R.id.toolbar))
        loadMemberList()
    }

    private fun loadMemberList() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewAdapter = PartyMemberRecyclerViewAdapter(null, true)
        viewAdapter?.getUserClickedEvents()?.subscribe(Consumer { userId ->
            val resultIntent = Intent()
            resultIntent.putExtra("member_id", userId)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }, RxErrorHandler.handleEmptyError())?.let { compositeSubscription.add(it) }
        recyclerView.adapter = viewAdapter

        compositeSubscription.add(userRepository.getUser()
                .firstElement()
                .flatMap { user -> socialRepository.getGroupMembers(user.party?.id ?: "").firstElement() }
                .subscribe(Consumer { viewAdapter?.updateData(it) }, RxErrorHandler.handleEmptyError()))
    }
}
