package com.piloterr.android.piloterr.ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.piloterr.android.piloterr.PiloterrApplication
import com.piloterr.android.piloterr.PiloterrBaseApplication
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.events.ShowConnectionProblemEvent
import com.piloterr.android.piloterr.extensions.getThemeColor
import com.piloterr.android.piloterr.helpers.LanguageHelper
import com.piloterr.android.piloterr.ui.views.dialogs.PiloterrAlertDialog
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*


abstract class BaseActivity : AppCompatActivity() {

    private var currentTheme: String? = null
    internal var forcedTheme: String? = null
    private var destroyed: Boolean = false

    protected abstract fun getLayoutResId(): Int

    open fun getContentView(): View {
        return (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(getLayoutResId(), null)
    }

    protected var compositeSubscription = CompositeDisposable()

    private val piloterrApplication: PiloterrApplication
        get() = application as PiloterrApplication

    var isActivityVisible = false

    override fun isDestroyed(): Boolean {
        return destroyed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val languageHelper = LanguageHelper(sharedPreferences.getString("language", "en"))
        Locale.setDefault(languageHelper.locale)
        val configuration = Configuration()
        configuration.setLocale(languageHelper.locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        loadTheme(sharedPreferences)

        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        super.onCreate(savedInstanceState)
        piloterrApplication
        injectActivity(PiloterrBaseApplication.userComponent)
        setContentView(getContentView())
        compositeSubscription = CompositeDisposable()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true
        loadTheme(PreferenceManager.getDefaultSharedPreferences(this))
    }

    override fun onPause() {
        isActivityVisible = false
        super.onPause()
    }

    override fun onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onStop()
    }

    private fun loadTheme(sharedPreferences: SharedPreferences) {
        val theme = if (forcedTheme != null) {
            forcedTheme
        } else {
            sharedPreferences.getString("theme_name", "purple")
        }
        if (theme == currentTheme) return
        setTheme(when (theme) {
            "maroon" -> R.style.MainAppTheme_Maroon
            "red" -> R.style.MainAppTheme_Red
            "orange" -> R.style.MainAppTheme_Orange
            "yellow" -> R.style.MainAppTheme_Yellow
            "green" -> R.style.MainAppTheme_Green
            "teal" -> R.style.MainAppTheme_Teal
            "blue" -> R.style.MainAppTheme_Blue
            else -> R.style.MainAppTheme
        })
        window.navigationBarColor = getThemeColor(R.attr.colorPrimaryDark)
        window.statusBarColor = getThemeColor(R.attr.colorPrimaryDark)

        if (currentTheme != null) {
            reload()
        } else {
            currentTheme = theme
        }
    }

    protected abstract fun injectActivity(component: UserComponent?)

    protected fun setupToolbar(toolbar: Toolbar?) {
        if (toolbar != null) {
            setSupportActionBar(toolbar)

            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setDisplayShowHomeEnabled(true)
                actionBar.setDisplayShowTitleEnabled(true)
                actionBar.setDisplayUseLogoEnabled(false)
                actionBar.setHomeButtonEnabled(true)
            }
        }
    }

    override fun onDestroy() {
        destroyed = true

        if (!compositeSubscription.isDisposed) {
            compositeSubscription.dispose()
        }
        super.onDestroy()
    }

    @Subscribe
    open fun onEvent(event: ShowConnectionProblemEvent) {
        val alert = PiloterrAlertDialog(this)
        alert.setTitle(event.title)
        alert.setMessage(event.message)
        alert.addButton(android.R.string.ok, isPrimary = true, isDestructive = false, function = null)
        alert.enqueue()
    }

    fun reload() {
        finish()
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
        startActivity(intent)
    }
}
