package com.mobile.myshop.datamodel.usecase

import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.request.RegisterRequest
import com.mobile.myshop.datamodel.response.RegisterResponse
import com.mobile.myshop.utils.RxRetryWithDelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/3/2018.
 */
class RegisterUseCase(private val apiManager: ApiManager) {

    fun register(registerRequest: RegisterRequest): Observable<RegisterResponse> {
        with(registerRequest){
            return apiManager.requestRegister(fullName, phoneNumber, password, deviceId)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(RxRetryWithDelay(3, 1000))
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}