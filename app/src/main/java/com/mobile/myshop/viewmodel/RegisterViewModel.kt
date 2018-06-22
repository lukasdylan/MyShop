package com.mobile.myshop.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import android.text.Editable
import com.mobile.myshop.R
import com.mobile.myshop.datamodel.request.RegisterRequest
import com.mobile.myshop.datamodel.usecase.RegisterUseCase
import com.mobile.myshop.utils.ResourceProvider
import com.mobile.myshop.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import java.io.EOFException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/3/2018.
 */

class RegisterViewModel(private val registerUseCase: RegisterUseCase, private val resourceProvider: ResourceProvider) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var registerRequest = RegisterRequest()
    var errorFullName = ObservableField<String>(null)
    var errorPhoneNumber = ObservableField<String>(null)
    var errorPassword = ObservableField<String>(null)
    var dialogState = SingleLiveEvent<Pair<Int, String>>()
    var errorMessage = SingleLiveEvent<String>()
    var currentDialogState = 0
    var hideKeyboard = SingleLiveEvent<Void>()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun onFullNameTextChanged(editable: Editable) {
        registerRequest.fullName = editable.toString()
    }

    fun onPhoneNumberTextChanged(editable: Editable) {
        registerRequest.phoneNumber = editable.toString()
    }

    fun onPasswordTextChanged(editable: Editable) {
        registerRequest.password = editable.toString()
    }

    private fun isValidData(): Boolean {
        var valid = true
        if (registerRequest.fullName.isBlank() || registerRequest.fullName.length <= 2) {
            errorFullName.set("Cek kembali nama anda")
            valid = false
        }
        if (registerRequest.phoneNumber.length <= 10 || registerRequest.phoneNumber.length > 12) {
            errorPhoneNumber.set("Nomor handphone anda tidak valid")
            valid = false
        }
        if (registerRequest.password.length < 6) {
            errorPassword.set("Minimal jumlah karakter untuk password anda adalah 6")
            valid = false
        }
        return valid
    }

    fun onConfirmationData() {
        if (isValidData()) {
            hideKeyboard.call()
            currentDialogState = CONFIRMATION_STATE
            dialogState.value = Pair(currentDialogState, "Mohon cek kembali data anda\n\nNama lengkap: " + registerRequest.fullName
                    + "\nNomor Handphone: " + registerRequest.phoneNumber + "\n\nApakah data anda sudah benar?")
        }
    }

    fun onRegisterRequest(deviceId: String?) {
        registerRequest.deviceId = deviceId.toString()
        compositeDisposable.add(
                registerUseCase.register(registerRequest)
                        .doOnSubscribe {
                            errorFullName.set(null)
                            errorPhoneNumber.set(null)
                            errorPassword.set(null)
                            currentDialogState = LOADING_STATE
                            dialogState.value = Pair(currentDialogState, "Mohon Tunggu...")
                        }
                        .subscribe({
                            if (it.statusCode == 1) {
                                currentDialogState = SUCCESS_STATE
                                dialogState.value = Pair(currentDialogState, "Pendaftaran anda sukses, silahkan menunggu konfirmasi dari pihak toko")
                            } else {
                                currentDialogState = FAILED_STATE
                                dialogState.value = Pair(currentDialogState, "Registrasi tidak berhasil...")
                            }
                        }, {
                            currentDialogState = FAILED_STATE
                            dialogState.value = Pair(currentDialogState, "Registrasi tidak berhasil...")
                            if (it is SocketException || it is SocketTimeoutException) {
                                errorMessage.value = (resourceProvider.getString(R.string.msg_error_connection))
                            } else if (it is UnknownHostException || it is EOFException) {
                                errorMessage.value = ("Ada masalah dengan server. Silahkan coba kembali")
                            } else {
                                errorMessage.value = (it.localizedMessage)
                            }
                        }))

    }

    companion object {
        const val CONFIRMATION_STATE = 0
        const val LOADING_STATE = 1
        const val SUCCESS_STATE = 2
        const val FAILED_STATE = 3
    }
}

class RegisterViewModelFactory(private val registerUseCase: RegisterUseCase,
                               private val resourceProvider: ResourceProvider) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerUseCase, resourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}