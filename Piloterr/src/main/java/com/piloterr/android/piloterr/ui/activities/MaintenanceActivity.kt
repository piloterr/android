package com.piloterr.android.piloterr.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.net.toUri
import com.facebook.drawee.view.SimpleDraweeView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.api.MaintenanceApiService
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.ApiClient
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.helpers.setMarkdown
import com.piloterr.android.piloterr.ui.views.PiloterrEmojiTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MaintenanceActivity : BaseActivity() {

    @Inject
    lateinit var maintenanceService: MaintenanceApiService

    @Inject
    lateinit var apiClient: ApiClient

    internal val titleTextView: TextView by bindView(R.id.titleTextView)
    internal val imageView: SimpleDraweeView by bindView(R.id.imageView)
    internal val descriptionTextView: PiloterrEmojiTextView by bindView(R.id.descriptionTextView)
    internal val playStoreButton: Button by bindView(R.id.playStoreButton)
    private var isDeprecationNotice: Boolean = false

    override fun getLayoutResId(): Int {
        return R.layout.activity_maintenance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.extras ?: return

        this.titleTextView.text = data.getString("title")

        @Suppress("DEPRECATION")
        imageView.setImageURI(data.getString("imageUrl")?.toUri())
        this.descriptionTextView.setMarkdown(data.getString("description"))
        this.descriptionTextView.movementMethod = LinkMovementMethod.getInstance()

        isDeprecationNotice = data.getBoolean("deprecationNotice")
        if (isDeprecationNotice) {
            this.playStoreButton.visibility = View.VISIBLE
        } else {
            this.playStoreButton.visibility = View.GONE
        }

        playStoreButton.setOnClickListener { openInPlayStore() }
    }

    override fun injectActivity(component: UserComponent?) {
        component?.inject(this)
    }

    override fun onResume() {
        super.onResume()
        if (!isDeprecationNotice) {
            compositeSubscription.add(this.maintenanceService.maintenanceStatus
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer { maintenanceResponse ->
                        if (!maintenanceResponse.activeMaintenance) {
                            finish()
                        }
                    }, RxErrorHandler.handleEmptyError()))
        }
    }

    private fun openInPlayStore() {
        val appPackageName = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=$appPackageName".toUri()))
        } catch (anfe: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$appPackageName".toUri()))
        }

    }
}
