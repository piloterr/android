package com.piloterr.android.piloterr.ui.adapter.social

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.models.social.Challenge
import com.piloterr.android.piloterr.models.social.ChallengeMembership
import com.piloterr.android.piloterr.ui.fragments.social.challenges.ChallengeFilterOptions
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.views.PiloterrEmojiTextView
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import net.pherth.android.emoji_library.EmojiParser

class ChallengesListViewAdapter(data: OrderedRealmCollection<Challenge>?, autoUpdate: Boolean, private val viewUserChallengesOnly: Boolean, private val userId: String) : RealmRecyclerViewAdapter<Challenge, ChallengesListViewAdapter.ChallengeViewHolder>(data, autoUpdate) {
    private var unfilteredData: OrderedRealmCollection<Challenge>? = null
    var challengeMemberships: OrderedRealmCollection<ChallengeMembership>? = null

    private val openChallengeFragmentEvents = PublishSubject.create<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        return ChallengeViewHolder(parent.inflate(R.layout.challenge_item), viewUserChallengesOnly)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        data?.get(position)?.let { challenge ->
            holder.bind(challenge, challengeMemberships?.first { challenge.id == it.challengeID } != null)
            holder.itemView.setOnClickListener {
                if (challenge.isManaged && challenge.isValid) {
                    challenge.id?.let {
                        openChallengeFragmentEvents.onNext(it)
                    }
                }
            }
        }
    }

    fun updateUnfilteredData(data: OrderedRealmCollection<Challenge>?) {
        super.updateData(data)
        unfilteredData = data
    }

    fun filter(filterOptions: ChallengeFilterOptions) {
        if (unfilteredData == null) {
            return
        }

        var query = unfilteredData?.where()

        if (filterOptions.showByGroups != null && filterOptions.showByGroups.size > 0) {
            val groupIds = arrayOfNulls<String>(filterOptions.showByGroups.size)
            var index = 0
            for (group in filterOptions.showByGroups) {
                groupIds[index] = group.id
                index += 1
            }
            query = query?.`in`("groupId", groupIds)
        }

        if (filterOptions.showOwned != filterOptions.notOwned) {
            query = if (filterOptions.showOwned) {
                query?.equalTo("leaderId", userId)
            } else {
                query?.notEqualTo("leaderId", userId)
            }
        }

        query?.let {
            this.updateData(it.findAll())
        }
    }

    fun getOpenDetailFragmentFlowable(): Flowable<String> {
        return openChallengeFragmentEvents.toFlowable(BackpressureStrategy.DROP)
    }

    class ChallengeViewHolder internal constructor(itemView: View, private val viewUserChallengesOnly: Boolean) : RecyclerView.ViewHolder(itemView) {

        private val challengeName: PiloterrEmojiTextView by bindView(R.id.challenge_name)
        private val challengeDescription: TextView by bindView(R.id.challenge_shorttext)
        private val participantCount: TextView by bindView(R.id.participantCount)
        private val officialChallengeLayout: TextView by bindView(R.id.official_challenge_view)
        private val challengeParticipatingTextView: View by bindView(R.id.is_joined_label)
        private val gemPrizeTextView: TextView by bindView(R.id.gemPrizeTextView)
        private val gemIconView: ImageView by bindView(R.id.gem_icon)

        private var challenge: Challenge? = null

        init {
            gemIconView.setImageBitmap(PiloterrIconsHelper.imageOfGem())

            if (!viewUserChallengesOnly) {
                challengeName.setTextColor(ContextCompat.getColor(itemView.context, R.color.brand_200))
            }
        }

        fun bind(challenge: Challenge, isParticipating: Boolean) {
            this.challenge = challenge

            challengeName.text = EmojiParser.parseEmojis(challenge.name?.trim { it <= ' ' })
            challengeDescription.text = challenge.summary

            officialChallengeLayout.visibility = if (challenge.official) View.VISIBLE else View.GONE

            if (viewUserChallengesOnly) {
                challengeParticipatingTextView.visibility = View.GONE
            } else {
                challengeParticipatingTextView.visibility = if (isParticipating) View.VISIBLE else View.GONE
            }
            participantCount.text = challenge.memberCount.toString()

            gemPrizeTextView.text = challenge.prize.toString()
        }
    }
}
