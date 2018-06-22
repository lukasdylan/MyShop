package com.mobile.myshop.datamodel.usecase

import com.mobile.myshop.datamodel.constant.AppConstant
import com.mobile.myshop.datamodel.remote.RajaOngkirApiManager
import com.mobile.myshop.datamodel.response.CityResult
import com.mobile.myshop.datamodel.response.ProvinceResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/2/2018.
 */
class AdministrativeUseCase(private val rajaOngkirApiManager: RajaOngkirApiManager) {

    fun fetchProvinceList(): Observable<List<ProvinceResult>> {
        return rajaOngkirApiManager.requestListProvince(AppConstant.RAJA_ONGKIR_API_KEY)
                .filter { it.rajaongkir?.status?.code == 200 }
                .flatMap {
                    return@flatMap it.rajaongkir?.let {
                        Observable.just(it.results)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchRegionList(provinceCode: String): Observable<List<CityResult>> {
        return rajaOngkirApiManager.requestListCityByProvince(AppConstant.RAJA_ONGKIR_API_KEY, provinceCode)
                .filter { it.rajaongkir?.status?.code == 200 }
                .flatMap {
                    return@flatMap it.rajaongkir?.let {
                        Observable.just(it.results)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}