package com.mobile.myshop.datamodel.usecase

import com.mobile.myshop.datamodel.constant.AppConstant
import com.mobile.myshop.datamodel.remote.RajaOngkirApiManager
import com.mobile.myshop.datamodel.request.ShippingCostRequest
import com.mobile.myshop.datamodel.response.CostResult
import com.mobile.myshop.utils.RxRetryWithDelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/1/2018.
 */
class ShippingCostUseCase(private val rajaOngkirApiManager: RajaOngkirApiManager) {

    fun calculateShippingCost(shippingCostRequest: ShippingCostRequest): Observable<List<CostResult>> {
        return rajaOngkirApiManager
                .requestShippingCost(
                        AppConstant.RAJA_ONGKIR_API_KEY,
                        AppConstant.RAJA_ONGKIR_API_ORIGIN_CODE,
                        shippingCostRequest.cityCode,
                        shippingCostRequest.weightTotal,
                        shippingCostRequest.courierName)
                .filter { it.rajaongkir?.status?.code == 200 }
                .flatMap {
                    return@flatMap it.rajaongkir?.let {
                        Observable.just(it.results[0].costs)
                    }
                }
                .subscribeOn(Schedulers.io())
                .retryWhen(RxRetryWithDelay(3, 1000))
                .observeOn(AndroidSchedulers.mainThread())
    }
}