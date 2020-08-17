package com.piloterr.android.piloterr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import com.amplitude.api.Amplitude
import com.amplitude.api.Identify
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.piloterr.android.piloterr.api.HostConfig
import com.piloterr.android.piloterr.components.AppComponent
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.ApiClient
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.modules.UserModule
import com.piloterr.android.piloterr.modules.UserRepositoryModule
import com.piloterr.android.piloterr.proxy.CrashlyticsProxy
import com.piloterr.android.piloterr.ui.activities.IntroActivity
import com.piloterr.android.piloterr.ui.activities.LoginActivity
import com.piloterr.android.piloterr.ui.helpers.MarkdownParser
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import io.realm.Realm
import io.realm.RealmConfiguration
import org.solovyev.android.checkout.Billing
import org.solovyev.android.checkout.Cache
import org.solovyev.android.checkout.Checkout
import org.solovyev.android.checkout.PurchaseVerifier
import javax.inject.Inject

//contains all PiloterrApplicationLogic except dagger componentInitialisation
abstract class PiloterrBaseApplication : MultiDexApplication() {
    var refWatcher: RefWatcher? = null
    @Inject
    internal lateinit var lazyApiHelper: ApiClient
    @Inject
    internal lateinit var sharedPrefs: SharedPreferences
    @Inject
    internal lateinit var crashlyticsProxy: CrashlyticsProxy
    /**
     * For better performance billing class should be used as singleton
     */
    // endregion

    var billing: Billing? = null
        private set
    /**
     * Application wide [Checkout] instance (can be used
     * anywhere in the app).
     * This instance contains all available products in the app.
     */
    var checkout: Checkout? = null
        private set

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        setupRealm()
        setupDagger()
        setupRemoteConfig()
        setupNotifications()
        refWatcher = LeakCanary.install(this)
        createBillingAndCheckout()
        PiloterrIconsHelper.init(this)
        MarkdownParser.setup(this)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        if (!BuildConfig.DEBUG) {
            try {
                Amplitude.getInstance().initialize(this, getString(R.string.amplitude_app_id)).enableForegroundTracking(this)
                val identify = Identify().setOnce("androidStore", BuildConfig.STORE)
                Amplitude.getInstance().identify(identify)
            } catch (ignored: Resources.NotFoundException) {
            }

        }
        val config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)

        RxErrorHandler.init(crashlyticsProxy)

        FirebaseAnalytics.getInstance(this).setUserProperty("app_testing_level", BuildConfig.TESTING_LEVEL)

        checkIfNewVersion()
    }

    protected open fun setupRealm() {
        Realm.init(this)
        val builder = RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
        try {
            Realm.setDefaultConfiguration(builder.build())
        } catch (ignored: UnsatisfiedLinkError) {
            //Catch crash in tests
        }

    }

    private fun checkIfNewVersion() {
        var info: PackageInfo? = null
        try {
            info = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("MyApplication", "couldn't get package info!")
        }

        if (info == null) {
            return
        }

        val lastInstalledVersion = sharedPrefs.getInt("last_installed_version", 0)
        @Suppress("DEPRECATION")
        if (lastInstalledVersion < info.versionCode) {
            @Suppress("DEPRECATION")
            sharedPrefs.edit {
                putInt("last_installed_version", info.versionCode)
            }
        }
    }

    private fun setupDagger() {
        component = initDagger()
        reloadUserComponent()
        component?.inject(this)
    }

    protected abstract fun initDagger(): AppComponent

    override fun openOrCreateDatabase(name: String,
                                      mode: Int, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase {
        return super.openOrCreateDatabase(getDatabasePath(name).absolutePath, mode, factory)
    }

    override fun openOrCreateDatabase(name: String,
                                      mode: Int, factory: SQLiteDatabase.CursorFactory?, errorHandler: DatabaseErrorHandler?): SQLiteDatabase {
        return super.openOrCreateDatabase(getDatabasePath(name).absolutePath, mode, factory, errorHandler)
    }

    // endregion

    // region IAP - Specific

    override fun deleteDatabase(name: String): Boolean {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm1 ->
            realm1.deleteAll()
            realm1.close()
        }
        return true
    }

    private fun createBillingAndCheckout() {
        billing = Billing(this, object : Billing.DefaultConfiguration() {
            override fun getPublicKey(): String {
                return "DONT-NEED-IT"
            }

            override fun getCache(): Cache? {
                return Billing.newCache()
            }

            override fun getPurchaseVerifier(): PurchaseVerifier {
                return PiloterrPurchaseVerifier(this@PiloterrBaseApplication, lazyApiHelper)
            }
        })

        billing?.let { checkout = Checkout.forApplication(it) }
    }

    private fun setupRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) 0 else 3600)
                .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaults(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
    }

    private fun setupNotifications() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "getInstanceId failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new Instance ID token
            val token = task.result?.token

            // Log and toast
            if (BuildConfig.DEBUG) {
                Log.d("Token", "Firebase Notification Token: $token")
            }
        }
    }

    companion object {

        var component: AppComponent? = null
            private set

        var userComponent: UserComponent? = null

        fun getInstance(context: Context): PiloterrBaseApplication? {
            return context.applicationContext as? PiloterrBaseApplication
        }

        fun logout(context: Context) {
            val realm = Realm.getDefaultInstance()
            getInstance(context)?.deleteDatabase(realm.path)
            realm.close()
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val useReminder = preferences.getBoolean("use_reminder", false)
            val reminderTime = preferences.getString("reminder_time", "19:00")
            preferences.edit {
                clear()
                putBoolean("use_reminder", useReminder)
                putString("reminder_time", reminderTime)
            }
            reloadUserComponent()
            getInstance(context)?.lazyApiHelper?.updateAuthenticationCredentials(null, null)
            startActivity(LoginActivity::class.java, context)
        }

        fun reloadUserComponent() {
            userComponent = component?.plus(UserModule(), UserRepositoryModule())
        }

        fun checkUserAuthentication(context: Context, hostConfig: HostConfig?): Boolean {
            if (hostConfig?.apiKey == null || hostConfig.apiKey == "" || hostConfig.userID == "") {
                startActivity(IntroActivity::class.java, context)
                return false
            }

            return true
        }

        private fun startActivity(activityClass: Class<*>, context: Context) {
            val intent = Intent(context, activityClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
