package com.piloterr.android.piloterr.ui.adapter.social

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.data.SocialRepository
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.helpers.MainNavigationController
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.social.Group
import com.piloterr.android.piloterr.ui.fragments.social.PublicGuildsFragmentDirections
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.helpers.setMarkdown
import io.reactivex.functions.Consumer
import io.realm.Case
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class PublicGuildsRecyclerViewAdapter(data: OrderedRealmCollection<Group>?, autoUpdate: Boolean) : RealmRecyclerViewAdapter<Group, PublicGuildsRecyclerViewAdapter.GuildViewHolder>(data, autoUpdate), Filterable {

    var socialRepository: SocialRepository? = null
    private var memberGuildIDs: List<String> = listOf()

    fun setMemberGuildIDs(memberGuildIDs: List<String>) {
        this.memberGuildIDs = memberGuildIDs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuildViewHolder {
        val guildViewHolder = GuildViewHolder(parent.inflate(R.layout.item_public_guild))
        guildViewHolder.itemView.setOnClickListener { v ->
            val guild = v.tag as? Group ?: return@setOnClickListener
            val directions = PublicGuildsFragmentDirections.openGuildDetail(guild.id)
            directions.isMember = isInGroup(guild)
            MainNavigationController.navigate(directions)
        }
        guildViewHolder.joinLeaveButton.setOnClickListener { v ->
            val guild = v.tag as? Group ?: return@setOnClickListener
            val isMember = this.memberGuildIDs.contains(guild.id)
            if (isMember) {
                this@PublicGuildsRecyclerViewAdapter.socialRepository?.leaveGroup(guild.id, true)
                        ?.subscribe(Consumer {
                            if (data != null) {
                                val indexOfGroup = data?.indexOf(guild)
                                notifyItemChanged(indexOfGroup ?: 0)
                            }
                        }, RxErrorHandler.handleEmptyError())
            } else {
                this@PublicGuildsRecyclerViewAdapter.socialRepository?.joinGroup(guild.id)
                        ?.subscribe(Consumer { group ->
                            if (data != null) {
                                val indexOfGroup = data?.indexOf(group)
                                notifyItemChanged(indexOfGroup ?: 0)
                            }
                        }, RxErrorHandler.handleEmptyError())
            }

        }
        return guildViewHolder
    }

    override fun onBindViewHolder(holder: GuildViewHolder, position: Int) {
        data?.let {
            val guild = it[position]
            val isInGroup = isInGroup(guild)
            holder.bind(guild, isInGroup)
            holder.itemView.tag = guild
            holder.joinLeaveButton.tag = guild
        }
    }

    private fun isInGroup(guild: Group): Boolean {
        return this.memberGuildIDs.contains(guild.id)
    }

    private var unfilteredData: OrderedRealmCollection<Group>? = null

    fun setUnfilteredData(data: OrderedRealmCollection<Group>?) {
        updateData(data)
        unfilteredData = data
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                results.values = constraint
                return FilterResults()
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                unfilteredData?.let {
                    if (constraint.isNotEmpty()) {
                        updateData(it.where()
                                .beginGroup()
                                .contains("name", constraint.toString(), Case.INSENSITIVE)
                                .or()
                                .contains("summary", constraint.toString(), Case.INSENSITIVE)
                                .endGroup()
                                .findAll())
                    }
                }
            }
        }
    }

    class GuildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTextView: TextView by bindView(R.id.nameTextView)
        private val memberCountTextView: TextView by bindView(R.id.memberCountTextView)
        private val descriptionTextView: TextView by bindView(R.id.descriptionTextView)
        internal val joinLeaveButton: Button by bindView(R.id.joinleaveButton)


        fun bind(guild: Group, isInGroup: Boolean) {
            this.nameTextView.text = guild.name
            this.memberCountTextView.text = guild.memberCount.toString()
            this.descriptionTextView.setMarkdown(guild.summary)
            if (isInGroup) {
                this.joinLeaveButton.setText(R.string.leave)
            } else {
                this.joinLeaveButton.setText(R.string.join)
            }
        }
    }
}
