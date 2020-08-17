package com.piloterr.android.piloterr.extensions

import com.piloterr.android.piloterr.helpers.RxErrorHandler
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

fun <T> Flowable<T>.subscribeWithErrorHandler(function: Consumer<T>): Disposable {
    return subscribe(function, RxErrorHandler.handleEmptyError())
}