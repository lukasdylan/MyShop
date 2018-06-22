package com.mobile.myshop.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import android.os.Bundle
import android.text.Editable
import com.mobile.myshop.R
import com.mobile.myshop.datamodel.entity.Customer
import com.mobile.myshop.datamodel.request.LoginRequest
import com.mobile.myshop.datamodel.usecase.LoginUseCase
import com.mobile.myshop.utils.ResourceProvider
import com.mobile.myshop.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.io.EOFException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/30/2018.
 */
class LoginViewModel(private val loginUseCase: LoginUseCase, private val resourceProvider: ResourceProvider) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var errorPhoneNumber = ObservableField<String>(null)
    var errorPassword = ObservableField<String>(null)
    var errorMessage = MutableLiveData<String>()
    var loading = SingleLiveEvent<Boolean>()
    var screenNavigation = SingleLiveEvent<Pair<Int, Bundle>>()
    var hideKeyboard = SingleLiveEvent<Void>()
    private val loginRequest = LoginRequest()

    private fun isValidData(): Boolean {
        var valid = true
        if (loginRequest.phoneNumber.trim().length < 11 || loginRequest.phoneNumber.trim().length > 15) {
            errorPhoneNumber.set("Mohon cek kembali nomor handphone anda")
            valid = false
        }
        if (loginRequest.password.trim().length < 4 || loginRequest.password.trim().length > 10) {
            errorPassword.set("Mohon cek kembali password anda")
            valid = false
        }
        return valid
    }

    fun onPhoneNumberTextChange(editable: Editable) {
        loginRequest.phoneNumber = editable.toString()
    }

    fun onPasswordTextChange(editable: Editable) {
        loginRequest.password = editable.toString()
    }

    fun onRegisterButtonClicked(){
        hideKeyboard.call()
        screenNavigation.value = Pair(REGISTER_PAGE, Bundle.EMPTY)
    }

    fun onLoginRequest() {
        if (isValidData()) {
            compositeDisposable.add(loginUseCase.login(loginRequest)
                    .doOnSubscribe {
                        hideKeyboard.call()
                        loading.value = (true)
                        errorPhoneNumber.set(null)
                        errorPassword.set(null)
                        errorMessage.value = null
                    }
                    .doAfterTerminate {
                        errorMessage.value = null
                    }
                    .subscribe({
                        loading.value = (false)
                        if (it.statusCode == 1) {
                            it.customer?.let {
                                saveCustomerData(it[0])
                            }
                            screenNavigation.value = (Pair(MAIN_PAGE, Bundle()))
                        } else {
                            errorMessage.value = (it.message)
                        }
                    }, {
                        loading.value = (false)
                        if (it is SocketException || it is SocketTimeoutException) {
                            errorMessage.value = (resourceProvider.getString(R.string.msg_error_connection))
                        } else if (it is UnknownHostException || it is EOFException) {
                            errorMessage.value = ("Ada masalah dengan server. Silahkan coba kembali")
                        } else {
                            errorMessage.value = (it.localizedMessage)
                        }
                    }))
        }
    }

    private fun saveCustomerData(customer: Customer) {
        compositeDisposable.add(
                loginUseCase.saveCustomerData(customer)
                        .doOnError { Timber.e(it) }
                        .subscribe())
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    companion object {
        const val MAIN_PAGE = 0
        const val REGISTER_PAGE = 1
    }
}

class LoginViewModelFactory(private val loginUseCase: LoginUseCase, private val resourceProvider: ResourceProvider) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginUseCase, resourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
