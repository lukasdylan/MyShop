package com.mobile.myshop.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mobile.myshop.datamodel.usecase.AuthenticationUseCase
import com.mobile.myshop.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/18/2018.
 */

class AuthenticationViewModel(private val authenticationUseCase: AuthenticationUseCase) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var userFound = SingleLiveEvent<Boolean>()

    fun isAlreadyLoggedIn() {
        compositeDisposable.add(
                authenticationUseCase.checkCustomerData()
                        .subscribe({
                            userFound.value = true
                        }, {
                            userFound.value = false
                        }))
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}

@Suppress("UNCHECKED_CAST")
class AuthenticationViewModelFactory(private val authenticationUseCase: AuthenticationUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            return AuthenticationViewModel(authenticationUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}