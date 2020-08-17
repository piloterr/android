package com.piloterr.android.piloterr.data.local

import com.piloterr.android.piloterr.models.Achievement
import com.piloterr.android.piloterr.models.QuestAchievement
import com.piloterr.android.piloterr.models.Skill
import com.piloterr.android.piloterr.models.TutorialStep
import com.piloterr.android.piloterr.models.social.ChatMessage
import com.piloterr.android.piloterr.models.user.User
import io.reactivex.Flowable
import io.realm.RealmResults

interface UserLocalRepository : BaseLocalRepository {

    fun getTutorialSteps(): Flowable<RealmResults<TutorialStep>>

    fun getUser(userID: String): Flowable<User>

    fun saveUser(user: User)

    fun saveMessages(messages: List<ChatMessage>)

    fun getSkills(user: User): Flowable<RealmResults<Skill>>

    fun getSpecialItems(user: User): Flowable<RealmResults<Skill>>
    fun getAchievements(): Flowable<RealmResults<Achievement>>
    fun getQuestAchievements(userID: String): Flowable<RealmResults<QuestAchievement>>
    fun getIsUserOnQuest(userID: String): Flowable<Boolean>
}
