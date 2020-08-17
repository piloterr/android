package com.piloterr.android.piloterr.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.piloterr.android.piloterr.models.inventory.QuestCollect;
import com.piloterr.android.piloterr.models.inventory.QuestDropItem;
import com.piloterr.android.piloterr.models.tasks.TaskList;
import com.piloterr.android.piloterr.models.user.Purchases;
import com.piloterr.android.piloterr.utils.EquipmentListDeserializer;
import com.piloterr.android.piloterr.utils.PurchasedDeserializer;
import com.piloterr.android.piloterr.models.Achievement;
import com.piloterr.android.piloterr.models.ContentResult;
import com.piloterr.android.piloterr.models.FAQArticle;
import com.piloterr.android.piloterr.models.Notification;
import com.piloterr.android.piloterr.models.Skill;
import com.piloterr.android.piloterr.models.Tag;
import com.piloterr.android.piloterr.models.TutorialStep;
import com.piloterr.android.piloterr.models.WorldState;
import com.piloterr.android.piloterr.models.inventory.Customization;
import com.piloterr.android.piloterr.models.inventory.Equipment;
import com.piloterr.android.piloterr.models.inventory.Quest;
import com.piloterr.android.piloterr.models.members.Member;
import com.piloterr.android.piloterr.models.responses.FeedResponse;
import com.piloterr.android.piloterr.models.social.Challenge;
import com.piloterr.android.piloterr.models.social.ChatMessage;
import com.piloterr.android.piloterr.models.social.FindUsernameResult;
import com.piloterr.android.piloterr.models.social.Group;
import com.piloterr.android.piloterr.models.tasks.Task;
import com.piloterr.android.piloterr.models.user.OwnedItem;
import com.piloterr.android.piloterr.models.user.OwnedMount;
import com.piloterr.android.piloterr.models.user.OwnedPet;
import com.piloterr.android.piloterr.models.user.User;
import com.piloterr.android.piloterr.utils.AchievementListDeserializer;
import com.piloterr.android.piloterr.utils.BooleanAsIntAdapter;
import com.piloterr.android.piloterr.utils.ChallengeDeserializer;
import com.piloterr.android.piloterr.utils.ChallengeListDeserializer;
import com.piloterr.android.piloterr.utils.ChatMessageDeserializer;
import com.piloterr.android.piloterr.utils.ChatMessageListDeserializer;
import com.piloterr.android.piloterr.utils.ContentDeserializer;
import com.piloterr.android.piloterr.utils.CustomizationDeserializer;
import com.piloterr.android.piloterr.utils.DateDeserializer;
import com.piloterr.android.piloterr.utils.FAQArticleListDeserilializer;
import com.piloterr.android.piloterr.utils.FeedResponseDeserializer;
import com.piloterr.android.piloterr.utils.FindUsernameResultDeserializer;
import com.piloterr.android.piloterr.utils.GroupSerialization;
import com.piloterr.android.piloterr.utils.MemberSerialization;
import com.piloterr.android.piloterr.utils.OwnedItemListDeserializer;
import com.piloterr.android.piloterr.utils.OwnedMountListDeserializer;
import com.piloterr.android.piloterr.utils.NotificationDeserializer;
import com.piloterr.android.piloterr.utils.OwnedPetListDeserializer;
import com.piloterr.android.piloterr.utils.QuestCollectDeserializer;
import com.piloterr.android.piloterr.utils.QuestDeserializer;
import com.piloterr.android.piloterr.utils.QuestDropItemsListSerialization;
import com.piloterr.android.piloterr.utils.SkillDeserializer;
import com.piloterr.android.piloterr.utils.TaskListDeserializer;
import com.piloterr.android.piloterr.utils.TaskSerializer;
import com.piloterr.android.piloterr.utils.TaskTagDeserializer;
import com.piloterr.android.piloterr.utils.TutorialStepListDeserializer;
import com.piloterr.android.piloterr.utils.UserDeserializer;
import com.piloterr.android.piloterr.utils.WorldStateSerialization;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import retrofit2.converter.gson.GsonConverterFactory;

public class GSonFactoryCreator {

    public static GsonConverterFactory create() {
        Type skillListType = new TypeToken<List<Skill>>() {}.getType();
        Type taskTagClassListType = new TypeToken<RealmList<Tag>>() {}.getType();
        Type customizationListType = new TypeToken<RealmList<Customization>>() {}.getType();
        Type tutorialStepListType = new TypeToken<RealmList<TutorialStep>>() {}.getType();
        Type faqArticleListType = new TypeToken<RealmList<FAQArticle>>() {}.getType();
        Type itemDataListType = new TypeToken<RealmList<Equipment>>() {}.getType();
        Type questCollectListType = new TypeToken<RealmList<QuestCollect>>() {}.getType();
        Type chatMessageListType = new TypeToken<RealmList<ChatMessage>>() {}.getType();
        Type challengeListType = new TypeToken<List<Challenge>>() {}.getType();
        Type challengeRealmListType = new TypeToken<RealmList<Challenge>>() {}.getType();
        Type questDropItemListType = new TypeToken<RealmList<QuestDropItem>>() {}.getType();
        Type ownedItemListType = new TypeToken<RealmList<OwnedItem>>() {}.getType();
        Type ownedPetListType = new TypeToken<RealmList<OwnedPet>>() {}.getType();
        Type ownedMountListType = new TypeToken<RealmList<OwnedMount>>() {}.getType();
        Type achievementsListType = new TypeToken<List<Achievement>>() {}.getType();


        //Exclusion strategy needed for DBFlow https://github.com/Raizlabs/DBFlow/issues/121
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(taskTagClassListType, new TaskTagDeserializer())
                .registerTypeAdapter(Boolean.class, new BooleanAsIntAdapter())
                .registerTypeAdapter(boolean.class, new BooleanAsIntAdapter())
                .registerTypeAdapter(skillListType, new SkillDeserializer())
                .registerTypeAdapter(TaskList.class, new TaskListDeserializer())
                .registerTypeAdapter(Purchases.class, new PurchasedDeserializer())
                .registerTypeAdapter(customizationListType, new CustomizationDeserializer())
                .registerTypeAdapter(tutorialStepListType, new TutorialStepListDeserializer())
                .registerTypeAdapter(faqArticleListType, new FAQArticleListDeserilializer())
                .registerTypeAdapter(Group.class, new GroupSerialization())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(itemDataListType, new EquipmentListDeserializer())
                .registerTypeAdapter(ChatMessage.class, new ChatMessageDeserializer())
                .registerTypeAdapter(Task.class, new TaskSerializer())
                .registerTypeAdapter(ContentResult.class, new ContentDeserializer())
                .registerTypeAdapter(FeedResponse.class, new FeedResponseDeserializer())
                .registerTypeAdapter(Challenge.class, new ChallengeDeserializer())
                .registerTypeAdapter(User.class, new UserDeserializer())
                .registerTypeAdapter(questCollectListType, new QuestCollectDeserializer())
                .registerTypeAdapter(chatMessageListType, new ChatMessageListDeserializer())
                .registerTypeAdapter(challengeListType, new ChallengeListDeserializer())
                .registerTypeAdapter(challengeRealmListType, new ChallengeListDeserializer())
                .registerTypeAdapter(questDropItemListType, new QuestDropItemsListSerialization())
                .registerTypeAdapter(ownedItemListType, new OwnedItemListDeserializer())
                .registerTypeAdapter(ownedPetListType, new OwnedPetListDeserializer())
                .registerTypeAdapter(ownedMountListType, new OwnedMountListDeserializer())
                .registerTypeAdapter(achievementsListType, new AchievementListDeserializer())
                .registerTypeAdapter(Quest.class, new QuestDeserializer())
                .registerTypeAdapter(Member.class, new MemberSerialization())
                .registerTypeAdapter(WorldState.class, new WorldStateSerialization())
                .registerTypeAdapter(FindUsernameResult.class, new FindUsernameResultDeserializer())
                .registerTypeAdapter(Notification.class, new NotificationDeserializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        return GsonConverterFactory.create(gson);
    }
}
