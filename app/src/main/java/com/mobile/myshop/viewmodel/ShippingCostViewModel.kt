package com.mobile.myshop.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.mobile.myshop.datamodel.request.ShippingCostRequest
import com.mobile.myshop.datamodel.response.CostResult
import com.mobile.myshop.datamodel.usecase.ShippingCostUseCase
import com.mobile.myshop.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/1/2018.
 */

class ShippingCostViewModel(private val shippingCostUseCase: ShippingCostUseCase) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var provinceText = ObservableField<String>()
    var cityTextLayout = ObservableInt(View.INVISIBLE)
    var cityText = ObservableField<String>()
    var courierTextLayout = ObservableInt(View.INVISIBLE)
    var courierText = ObservableField<String>()
    var weightTextLayout = ObservableInt(View.INVISIBLE)
    var errorMessageWeight = ObservableField<String>()
    var calculateButtonLayout = ObservableInt(View.INVISIBLE)
    var shippingCostResultLayout = ObservableInt(View.GONE)
    var shippingCostResult = MutableLiveData<Pair<List<CostResult>, String>>()
    private var shippingCostRequest = ShippingCostRequest()
    var navigation = SingleLiveEvent<Pair<Int, Bundle>>()
    var uiEnable = ObservableBoolean(true)

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun onProvinceLayoutClicked() {
        val bundle = Bundle().apply {
            putInt("request_code", PROVINCE_REQUEST)
        }
        navigation.value = Pair(PROVINCE_REQUEST, bundle)
    }

    fun onCityLayoutClicked() {
        val bundle = Bundle().apply {
            putInt("request_code", CITY_REQUEST)
            putInt("province_code", shippingCostRequest.provinceCode.toInt())
        }
        navigation.value = Pair(CITY_REQUEST, bundle)
    }

    fun onCourierLayoutClicked() {
        navigation.value = Pair(COURIER_REQUEST, Bundle.EMPTY)
    }

    fun onInputWeight(editable: Editable) {
        if (editable.isNotEmpty()) {
            shippingCostRequest.weightTotalDouble = editable.toString().toDouble()
        } else {
            shippingCostRequest.weightTotalDouble = 0.0
        }
    }

    fun onAdministrativeDataResult(requestCode: Int, data: Intent) {
        when (requestCode) {
            PROVINCE_REQUEST -> {
                shippingCostRequest.provinceCode = data.getIntExtra("province_code", 0).toString()
                provinceText.set(String.format("Provinsi %s", data.getStringExtra("province_name")))
                cityTextLayout.set(View.VISIBLE)
                cityText.set(null)
                shippingCostRequest.cityCode = ""
                calculateButtonLayout.set(View.INVISIBLE)
                shippingCostResultLayout.set(View.GONE)
            }
            CITY_REQUEST -> {
                shippingCostRequest.cityCode = data.getIntExtra("city_code", 0).toString()
                cityText.set(data.getStringExtra("city_name"))
                courierTextLayout.set(View.VISIBLE)
            }
        }
    }

    fun onSelectedCourier(courierName: String) {
        weightTextLayout.set(View.VISIBLE)
        calculateButtonLayout.set(View.VISIBLE)
        courierText.set(courierName)
        when (courierName) {
            JNE_COURIER -> shippingCostRequest.courierName = "jne"
            TIKI_COURIER -> shippingCostRequest.courierName = "tiki"
            POS_INDONESIA_COURIER -> shippingCostRequest.courierName = "pos"
        }
    }

    fun loadShippingCostResult() {
        if (isValidRequest()) {
            errorMessageWeight.set(null)
            shippingCostRequest.weightTotal = (shippingCostRequest.weightTotalDouble * 1000).toInt()
            compositeDisposable.add(
                    shippingCostUseCase.calculateShippingCost(shippingCostRequest)
                            .doOnSubscribe {
                                shippingCostResultLayout.set(View.GONE)
                                uiEnable.set(false)
                            }
                            .doAfterTerminate { uiEnable.set(true) }
                            .subscribe({
                                shippingCostResultLayout.set(View.VISIBLE)
                                shippingCostResult.value = Pair(it, shippingCostRequest.courierName)
                            }, {
                                shippingCostResultLayout.set(View.GONE)
                            })
            )
        }
    }

    private fun isValidRequest(): Boolean {
        var valid = true
        if (shippingCostRequest.provinceCode.isEmpty()) {
            valid = false
        } else if (shippingCostRequest.cityCode.isEmpty()) {
            valid = false
        } else if (shippingCostRequest.courierName.isEmpty()) {
            valid = false
        } else if (shippingCostRequest.weightTotalDouble <= 30.0 && shippingCostRequest.weightTotalDouble == 0.0) {
            errorMessageWeight.set("Maksimal berat barang adalah 30kg")
            valid = false
        }
        return valid
    }

    companion object {
        const val PROVINCE_REQUEST = 0
        const val CITY_REQUEST = 1
        const val COURIER_REQUEST = 2
        const val JNE_COURIER = "JNE"
        const val TIKI_COURIER = "TIKI"
        const val POS_INDONESIA_COURIER = "Pos Indonesia"
    }
}

@Suppress("UNCHECKED_CAST")
class ShippingCostViewModelFactory(private val shippingCostUseCase: ShippingCostUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShippingCostViewModel::class.java)) {
            return ShippingCostViewModel(shippingCostUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}