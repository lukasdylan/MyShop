package com.mobile.myshop.datamodel.usecase

import com.mobile.myshop.datamodel.entity.Customer
import com.mobile.myshop.datamodel.repository.CustomerDao
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/18/2018.
 */
class AuthenticationUseCase(private val customerDao: CustomerDao) {

    fun checkCustomerData(): Single<Customer> {
        return customerDao.getCustomerData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}