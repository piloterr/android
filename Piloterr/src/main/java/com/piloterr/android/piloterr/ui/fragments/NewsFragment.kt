package com.piloterr.android.piloterr.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import com.piloterr.android.piloterr.BuildConfig
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.extensions.subscribeWithErrorHandler
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : BaseMainFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.hidesToolbar = true
        super.onCreateView(inflater, container, savedInstanceState)
        return container?.inflate(R.layout.fragment_news)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val address = if (BuildConfig.DEBUG) BuildConfig.BASE_URL else context?.getString(R.string.base_url)
        val webSettings = newsWebview.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        newsWebview.webChromeClient = object : WebChromeClient() {
        }
        newsWebview.loadUrl("$address/static/new-stuff")
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    override fun onResume() {
        super.onResume()
        compositeSubscription.add(userRepository.updateUser(user, "flags.newStuff", false).subscribeWithErrorHandler(Consumer {}))
    }
}
