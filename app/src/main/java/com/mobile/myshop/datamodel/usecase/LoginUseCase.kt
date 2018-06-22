package com.mobile.myshop.datamodel.usecase

import com.mobile.myshop.datamodel.entity.Customer
import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.repository.CustomerDao
import com.mobile.myshop.datamodel.request.LoginRequest
import com.mobile.myshop.datamodel.response.LoginResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/30/2018.
 */
class LoginUseCase(private val apiManager: ApiManager, private val customerDao: CustomerDao) {

    fun login(loginRequest: LoginRequest): Observable<LoginResponse> {
        return apiManager.requestLogin(loginRequest.phoneNumber, loginRequest.password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveCustomerData(customer: Customer): Completable {
        return Completable.fromAction { customerDao.setCustomerData(customer) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }
}