//package com.piloterr.android.piloterr.data.implementation;
//
//import com.piloterr.android.piloterr.BaseTestCase;
//import com.piloterr.android.piloterr.data.ApiClient;
//import com.piloterr.android.piloterr.data.TaskRepository;
//import com.piloterr.android.piloterr.data.local.UserLocalRepository;
//import com.piloterr.android.piloterr.models.tasks.TaskList;
//import com.piloterr.android.piloterr.models.tasks.TasksOrder;
//import com.piloterr.android.piloterr.models.user.Flags;
//import com.piloterr.android.piloterr.models.user.Items;
//import com.piloterr.android.piloterr.models.user.Preferences;
//import com.piloterr.android.piloterr.models.user.Stats;
//import com.piloterr.android.piloterr.models.user.User;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import rx.Observable;
//import rx.observers.TestSubscriber;
//
//import static junit.framework.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.anyMap;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class UserRepositoryImplTest extends BaseTestCase {
//
//    @Mock
//    private ApiClient mockedApiClient;
//    @Mock
//    private UserLocalRepository mockedLocalRepository;
//    @Mock
//    private TaskRepository mockedTaskRepository;
//    @InjectMocks
//    private UserRepositoryImpl userRepository;
//    private User testUser;
//
//
//    @Before
//    public void setUp() {
//        testUser = new User();
//        testUser.setId("123");
//        when(mockedLocalRepository.getUnmanagedCopy(testUser)).thenReturn(testUser);
//    }
//
//    @Test
//    public void testUpdatingUserSavesChanges() {
//        testUser.setStats(new Stats());
//        User newUser = new User();
//        newUser.setItems(new Items());
//        newUser.setPreferences(new Preferences());
//        newUser.setFlags(new Flags());
//        newUser.setStats(new Stats());
//        TestSubscriber<User> subscriber = new TestSubscriber<>();
//        when(mockedApiClient.updateUser(anyMap())).thenReturn(Observable.just(newUser));
//        userRepository.updateUser(testUser, "preferences.timezoneOffset", 2).toBlocking().subscribe(subscriber);
//        Map<String, Object> updateData = new HashMap<>();
//        updateData.put("preferences.timezoneOffset", 2);
//        verify(mockedApiClient).updateUser(updateData);
//        verify(mockedLocalRepository).saveUser(testUser);
//        assertEquals(testUser.getEquipment(), newUser.getEquipment());
//        assertEquals(testUser.getPreferences(), newUser.getPreferences());
//        assertEquals(testUser.getFlags(), newUser.getFlags());
//    }
//
//    @Test
//    public void testRetrievingUserSaves() {
//        when(mockedApiClient.retrieveUser(false)).thenReturn(Observable.just(testUser));
//        TestSubscriber<User> subscriber = new TestSubscriber<>();
//        userRepository.retrieveUser(false).toBlocking().subscribe(subscriber);
//        verify(mockedApiClient).retrieveUser(false);
//        verify(mockedLocalRepository).saveUser(testUser);
//        verify(mockedApiClient, never()).updateUser(anyMap());
//    }
//
//    @Test
//    public void testRetrievingUserUpdatesTimezone() {
//        testUser.setPreferences(new Preferences());
//        testUser.getPreferences().setTimezoneOffset(99);
//        when(mockedApiClient.retrieveUser(false)).thenReturn(Observable.just(testUser));
//        TestSubscriber<User> subscriber = new TestSubscriber<>();
//        userRepository.retrieveUser(false).toBlocking().subscribe(subscriber);
//        verify(mockedApiClient).retrieveUser(false);
//        verify(mockedLocalRepository).saveUser(testUser);
//        verify(mockedApiClient).updateUser(anyMap());
//    }
//
//    @Test
//    public void testRetrievingUserSavesTasks() {
//        testUser.setTasksOrder(new TasksOrder());
//        testUser.setTasks(new TaskList());
//        when(mockedApiClient.retrieveUser(false)).thenReturn(Observable.just(testUser));
//        TestSubscriber<User> subscriber = new TestSubscriber<>();
//        userRepository.retrieveUser(false).toBlocking().subscribe(subscriber);
//        subscriber.assertNoErrors();
//        verify(mockedApiClient).retrieveUser(false);
//        verify(mockedTaskRepository).saveTasks(testUser.getId(), testUser.getTasksOrder(), testUser.getTasks());
//    }
//
//    @Test
//    public void testRetrievingUserDoesntRetrieveTwice() {
//        when(mockedApiClient.retrieveUser(false)).thenReturn(Observable.just(testUser));
//        TestSubscriber<User> subscriber = new TestSubscriber<>();
//        userRepository.retrieveUser(false).toBlocking().subscribe(subscriber);
//        userRepository.retrieveUser(false).toBlocking().subscribe(subscriber);
//        verify(mockedApiClient).retrieveUser(false);
//    }
//}