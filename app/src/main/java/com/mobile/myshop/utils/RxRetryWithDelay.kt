package com.mobile.myshop.utils

import io.reactivex.Observable
import io.reactivex.functions.Function
import java.io.EOFException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/26/2018.
 */
class RxRetryWithDelay(private val maxRetries: Int, private val delayMillis: Long) : Function<Observable<out Throwable>, Observable<*>> {

    private var retryCount = 0

    override fun apply(t: Observable<out Throwable>): Observable<*> {
        return t.flatMap({
            if (++retryCount < maxRetries && (it is SocketTimeoutException || it is EOFException || it is UnknownHostException)) {
                Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
            } else {
                Observable.error(it)
            }
        })
    }
}