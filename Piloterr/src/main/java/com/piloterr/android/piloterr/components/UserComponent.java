package com.piloterr.android.piloterr.components;

import com.piloterr.android.piloterr.PiloterrBaseApplication;
import com.piloterr.android.piloterr.data.ApiClient;
import com.piloterr.android.piloterr.helpers.SoundManager;
import com.piloterr.android.piloterr.helpers.UserScope;
import com.piloterr.android.piloterr.helpers.notifications.PiloterrFirebaseMessagingService;
import com.piloterr.android.piloterr.helpers.notifications.PushNotificationManager;
import com.piloterr.android.piloterr.modules.UserModule;
import com.piloterr.android.piloterr.modules.UserRepositoryModule;
import com.piloterr.android.piloterr.receivers.LocalNotificationActionReceiver;
import com.piloterr.android.piloterr.receivers.NotificationPublisher;
import com.piloterr.android.piloterr.receivers.TaskAlarmBootReceiver;
import com.piloterr.android.piloterr.receivers.TaskReceiver;
import com.piloterr.android.piloterr.ui.activities.AboutActivity;
import com.piloterr.android.piloterr.ui.activities.AdventureGuideActivity;
import com.piloterr.android.piloterr.ui.activities.ChallengeFormActivity;
import com.piloterr.android.piloterr.ui.activities.ClassSelectionActivity;
import com.piloterr.android.piloterr.ui.activities.FixCharacterValuesActivity;
import com.piloterr.android.piloterr.ui.activities.FullProfileActivity;
import com.piloterr.android.piloterr.ui.activities.GemPurchaseActivity;
import com.piloterr.android.piloterr.ui.activities.GiftGemsActivity;
import com.piloterr.android.piloterr.ui.activities.GiftSubscriptionActivity;
import com.piloterr.android.piloterr.ui.activities.GroupFormActivity;
import com.piloterr.android.piloterr.ui.activities.GroupInviteActivity;
import com.piloterr.android.piloterr.ui.activities.HabitButtonWidgetActivity;
import com.piloterr.android.piloterr.ui.activities.IntroActivity;
import com.piloterr.android.piloterr.ui.activities.LoginActivity;
import com.piloterr.android.piloterr.ui.activities.MainActivity;
import com.piloterr.android.piloterr.ui.activities.MaintenanceActivity;
import com.piloterr.android.piloterr.ui.activities.NotificationsActivity;
import com.piloterr.android.piloterr.ui.activities.PrefsActivity;
import com.piloterr.android.piloterr.ui.activities.ReportMessageActivity;
import com.piloterr.android.piloterr.ui.activities.SetupActivity;
import com.piloterr.android.piloterr.ui.activities.SkillMemberActivity;
import com.piloterr.android.piloterr.ui.activities.SkillTasksActivity;
import com.piloterr.android.piloterr.ui.activities.TaskFormActivity;
import com.piloterr.android.piloterr.ui.activities.VerifyUsernameActivity;
import com.piloterr.android.piloterr.ui.adapter.social.challenges.ChallengeTasksRecyclerViewAdapter;
import com.piloterr.android.piloterr.ui.adapter.tasks.DailiesRecyclerViewHolder;
import com.piloterr.android.piloterr.ui.adapter.tasks.HabitsRecyclerViewAdapter;
import com.piloterr.android.piloterr.ui.adapter.tasks.RewardsRecyclerViewAdapter;
import com.piloterr.android.piloterr.ui.adapter.tasks.TodosRecyclerViewAdapter;
import com.piloterr.android.piloterr.ui.fragments.AboutFragment;
import com.piloterr.android.piloterr.ui.fragments.AchievementsFragment;
import com.piloterr.android.piloterr.ui.fragments.NavigationDrawerFragment;
import com.piloterr.android.piloterr.ui.fragments.NewsFragment;
import com.piloterr.android.piloterr.ui.fragments.StatsFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.customization.AvatarCustomizationFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.customization.AvatarEquipmentFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.customization.AvatarOverviewFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.equipment.EquipmentDetailFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.equipment.EquipmentOverviewFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.items.ItemRecyclerFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.items.ItemsFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.shops.ShopFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.shops.ShopsFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.stable.MountDetailRecyclerFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.stable.PetDetailRecyclerFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.stable.StableFragment;
import com.piloterr.android.piloterr.ui.fragments.inventory.stable.StableRecyclerFragment;
import com.piloterr.android.piloterr.ui.fragments.preferences.APIPreferenceFragment;
import com.piloterr.android.piloterr.ui.fragments.preferences.AuthenticationPreferenceFragment;
import com.piloterr.android.piloterr.ui.fragments.preferences.EmailNotificationsPreferencesFragment;
import com.piloterr.android.piloterr.ui.fragments.preferences.PreferencesFragment;
import com.piloterr.android.piloterr.ui.fragments.preferences.ProfilePreferencesFragment;
import com.piloterr.android.piloterr.ui.fragments.preferences.PushNotificationsPreferencesFragment;
import com.piloterr.android.piloterr.ui.fragments.purchases.GemsPurchaseFragment;
import com.piloterr.android.piloterr.ui.fragments.purchases.GiftBalanceGemsFragment;
import com.piloterr.android.piloterr.ui.fragments.purchases.GiftPurchaseGemsFragment;
import com.piloterr.android.piloterr.ui.fragments.purchases.SubscriptionFragment;
import com.piloterr.android.piloterr.ui.fragments.setup.AvatarSetupFragment;
import com.piloterr.android.piloterr.ui.fragments.setup.IntroFragment;
import com.piloterr.android.piloterr.ui.fragments.setup.TaskSetupFragment;
import com.piloterr.android.piloterr.ui.fragments.setup.WelcomeFragment;
import com.piloterr.android.piloterr.ui.fragments.skills.SkillTasksRecyclerViewFragment;
import com.piloterr.android.piloterr.ui.fragments.skills.SkillsFragment;
import com.piloterr.android.piloterr.ui.fragments.social.ChatFragment;
import com.piloterr.android.piloterr.ui.fragments.social.ChatListFragment;
import com.piloterr.android.piloterr.ui.fragments.social.GuildDetailFragment;
import com.piloterr.android.piloterr.ui.fragments.social.GuildFragment;
import com.piloterr.android.piloterr.ui.fragments.social.GuildsOverviewFragment;
import com.piloterr.android.piloterr.ui.fragments.social.InboxMessageListFragment;
import com.piloterr.android.piloterr.ui.fragments.social.InboxOverviewFragment;
import com.piloterr.android.piloterr.ui.fragments.social.NoPartyFragmentFragment;
import com.piloterr.android.piloterr.ui.fragments.social.PublicGuildsFragment;
import com.piloterr.android.piloterr.ui.fragments.social.QuestDetailFragment;
import com.piloterr.android.piloterr.ui.fragments.social.TavernDetailFragment;
import com.piloterr.android.piloterr.ui.fragments.social.TavernFragment;
import com.piloterr.android.piloterr.ui.fragments.social.challenges.ChallengeDetailFragment;
import com.piloterr.android.piloterr.ui.fragments.social.challenges.ChallengeListFragment;
import com.piloterr.android.piloterr.ui.fragments.social.challenges.ChallengesOverviewFragment;
import com.piloterr.android.piloterr.ui.fragments.social.party.PartyDetailFragment;
import com.piloterr.android.piloterr.ui.fragments.social.party.PartyFragment;
import com.piloterr.android.piloterr.ui.fragments.social.party.PartyInviteFragment;
import com.piloterr.android.piloterr.ui.fragments.support.BugFixFragment;
import com.piloterr.android.piloterr.ui.fragments.support.FAQDetailFragment;
import com.piloterr.android.piloterr.ui.fragments.support.FAQOverviewFragment;
import com.piloterr.android.piloterr.ui.fragments.support.SupportMainFragment;
import com.piloterr.android.piloterr.ui.fragments.tasks.TaskRecyclerViewFragment;
import com.piloterr.android.piloterr.ui.fragments.tasks.TasksFragment;
import com.piloterr.android.piloterr.ui.viewmodels.GroupViewModel;
import com.piloterr.android.piloterr.ui.viewmodels.InboxViewModel;
import com.piloterr.android.piloterr.ui.viewmodels.NotificationsViewModel;
import com.piloterr.android.piloterr.ui.views.insufficientCurrency.InsufficientGemsDialog;
import com.piloterr.android.piloterr.ui.views.shops.PurchaseDialog;
import com.piloterr.android.piloterr.ui.views.social.ChatBarView;
import com.piloterr.android.piloterr.ui.views.stats.BulkAllocateStatsDialog;
import com.piloterr.android.piloterr.ui.views.tasks.TaskFilterDialog;
import com.piloterr.android.piloterr.widget.AvatarStatsWidgetProvider;
import com.piloterr.android.piloterr.widget.BaseWidgetProvider;
import com.piloterr.android.piloterr.widget.DailiesWidgetProvider;
import com.piloterr.android.piloterr.widget.HabitButtonWidgetProvider;
import com.piloterr.android.piloterr.widget.HabitButtonWidgetService;
import com.piloterr.android.piloterr.widget.TaskListFactory;
import com.piloterr.android.piloterr.widget.TaskListWidgetProvider;

import org.jetbrains.annotations.NotNull;

import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {UserModule.class, UserRepositoryModule.class})
public interface UserComponent {
    void inject(ClassSelectionActivity classSelectionActivity);

    void inject(AboutActivity aboutActivity);

    void inject(GroupFormActivity groupFormActivity);

    void inject(IntroActivity introActivity);

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(MaintenanceActivity maintenanceActivity);

    void inject(GroupInviteActivity groupInviteActivity);

    void inject(PrefsActivity prefsActivity);

    void inject(NotificationsActivity notificationsActivity);

    void inject(SetupActivity setupActivity);

    void inject(SkillTasksActivity skillTasksActivity);

    void inject(SkillMemberActivity skillMembersActivity);

    void inject(TasksFragment tasksFragment);

    void inject(FAQOverviewFragment faqOverviewFragment);

    void inject(AvatarCustomizationFragment avatarCustomizationFragment);

    void inject(AvatarOverviewFragment avatarOverviewFragment);

    void inject(EquipmentDetailFragment equipmentDetailFragment);

    void inject(EquipmentOverviewFragment equipmentOverviewFragment);

    void inject(ItemRecyclerFragment itemRecyclerFragment);

    void inject(ItemsFragment itemsFragment);

    void inject(MountDetailRecyclerFragment mountDetailRecyclerFragment);

    void inject(PetDetailRecyclerFragment petDetailRecyclerFragment);

    void inject(StableFragment stableFragment);

    void inject(StableRecyclerFragment stableRecyclerFragment);

    void inject(AvatarSetupFragment avatarSetupFragment);

    void inject(IntroFragment introFragment);

    void inject(TaskSetupFragment taskSetupFragment);

    void inject(SkillsFragment skillsFragment);

    void inject(SkillTasksRecyclerViewFragment skillTasksRecyclerViewFragment);

    void inject(PartyFragment partyFragment);

    void inject(PartyInviteFragment partyInviteFragment);

    void inject(ChatListFragment chatListFragment);

    void inject(NoPartyFragmentFragment noPartyFragmentFragment);

    void inject(GuildFragment guildFragment);

    void inject(GuildsOverviewFragment guildsOverviewFragment);

    void inject(PublicGuildsFragment publicGuildsFragment);

    void inject(TavernFragment tavernFragment);

    void inject(TaskRecyclerViewFragment taskRecyclerViewFragment);

    void inject(GemsPurchaseFragment gemsPurchaseFragment);

    void inject(NewsFragment newsFragment);

    void inject(PiloterrBaseApplication piloterrApplication);

    void inject(PreferencesFragment preferencesFragment);

    void inject(InboxOverviewFragment inboxFragment);

    void inject(InboxMessageListFragment inboxMessageListFragment);

    void inject(ShopsFragment shopsFragment);

    void inject(ShopFragment shopFragment);

    void inject(PushNotificationManager pushNotificationManager);

    void inject(LocalNotificationActionReceiver localNotificationActionReceiver);

    void inject(FullProfileActivity fullProfileActivity);

    void inject(DailiesWidgetProvider dailiesWidgetProvider);

    void inject(HabitButtonWidgetService habitButtonWidgetService);

    void inject(HabitButtonWidgetActivity habitButtonWidgetActivity);

    void inject(HabitButtonWidgetProvider habitButtonWidgetProvider);

    void inject(AvatarStatsWidgetProvider avatarStatsWidgetProvider);

    void inject(SoundManager soundManager);

    void inject(ChallengesOverviewFragment challengesOverviewFragment);

    void inject(ChallengeListFragment challengeListFragment);

    void inject(ApiClient apiClient);

    void inject(TaskListWidgetProvider taskListWidgetProvider);

    void inject(DailiesRecyclerViewHolder dailiesRecyclerViewHolder);

    void inject(HabitsRecyclerViewAdapter habitsRecyclerViewAdapter);

    void inject(RewardsRecyclerViewAdapter rewardsRecyclerViewAdapter);

    void inject(TodosRecyclerViewAdapter todosRecyclerViewAdapter);

    void inject(SubscriptionFragment subscriptionFragment);

    void inject(ChallengeTasksRecyclerViewAdapter challengeTasksRecyclerViewAdapter);

    void inject(TaskListFactory taskListFactory);

    void inject(GemPurchaseActivity gemPurchaseActivity);

    void inject(TaskFilterDialog taskFilterDialog);

    void inject(TaskReceiver taskReceiver);

    void inject(TaskAlarmBootReceiver taskAlarmBootReceiver);

    void inject(PiloterrFirebaseMessagingService piloterrFirebaseMessagingService);

    void inject(BaseWidgetProvider baseWidgetProvider);

    void inject(NotificationPublisher notificationPublisher);

    void inject(ChallengeFormActivity challengeFormActivity);

    void inject(TavernDetailFragment tavernDetailFragment);

    void inject(PartyDetailFragment partyDetailFragment);

    void inject(QuestDetailFragment questDetailFragment);

    void inject(PurchaseDialog purchaseDialog);

    void inject(@NotNull FixCharacterValuesActivity fixCharacterValuesActivity);

    void inject(@NotNull AuthenticationPreferenceFragment authenticationPreferenceFragment);

    void inject(@NotNull ProfilePreferencesFragment profilePreferencesFragment);

    void inject(@NotNull APIPreferenceFragment apiPreferenceFragment);

    void inject(@NotNull StatsFragment statsFragment);

    void inject(@NotNull BulkAllocateStatsDialog bulkAllocateStatsDialog);

    void inject(@NotNull PushNotificationsPreferencesFragment pushNotificationsPreferencesFragment);

    void inject(WelcomeFragment welcomeFragment);

    void inject(@NotNull NavigationDrawerFragment navigationDrawerFragment);

    void inject(@NotNull ChallengeDetailFragment challengeDetailFragment);

    void inject(@NotNull VerifyUsernameActivity verifyUsernameActivity);

    void inject(@NotNull GroupViewModel viewModel);

    void inject(@NotNull NotificationsViewModel viewModel);

    void inject(@NotNull ChatFragment chatFragment);

    void inject(@NotNull GiftSubscriptionActivity giftSubscriptionActivity);

    void inject(@NotNull AboutFragment aboutFragment);

    void inject(@NotNull ChatBarView chatBarView);

    void inject(@NotNull TaskFormActivity taskFormActivity);

    void inject(@NotNull ReportMessageActivity reportMessageActivity);

    void inject(@NotNull GuildDetailFragment guildDetailFragment);

    void inject(@NotNull AchievementsFragment achievementsFragment);

    void inject(@NotNull InboxViewModel inboxViewModel);

    void inject(@NotNull InsufficientGemsDialog insufficientGemsDialog);

    void inject(@NotNull GiftGemsActivity giftGemsActivity);

    void inject(@NotNull GiftPurchaseGemsFragment giftPurchaseGemsFragment);

    void inject(@NotNull GiftBalanceGemsFragment giftBalanceGemsFragment);

    void inject(@NotNull EmailNotificationsPreferencesFragment emailNotificationsPreferencesFragment);

    void inject(@NotNull SupportMainFragment supportMainFragment);

    void inject(@NotNull BugFixFragment bugFixFragment);

    void inject(@NotNull AvatarEquipmentFragment avatarEquipmentFragment);

    void inject(@NotNull FAQDetailFragment faqDetailFragment);

    void inject(@NotNull AdventureGuideActivity adventureGuideFragment);
}
