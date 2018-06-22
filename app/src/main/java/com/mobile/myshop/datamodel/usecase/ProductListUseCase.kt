package com.mobile.myshop.datamodel.usecase

import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.response.ProductList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/3/2018.
 */
class ProductListUseCase(private val apiManager: ApiManager) {

    fun fetchProductList(): Observable<List<ProductList>> {
        return apiManager.requestProductList("2")
                .filter { it.productList != null && it.productList.isNotEmpty() }
                .flatMap { t ->
                    return@flatMap t.productList?.let {
                        Observable.just(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}