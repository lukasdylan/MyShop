package com.mobile.myshop.datamodel.usecase

import com.mobile.myshop.R
import com.mobile.myshop.datamodel.entity.FavoriteProduct
import com.mobile.myshop.datamodel.entity.MainMenu
import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.repository.CustomerDao
import com.mobile.myshop.datamodel.repository.FavoriteProductDao
import com.mobile.myshop.datamodel.response.ProductList
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 5/28/2018.
 */
class HomeUseCase(private val apiManager: ApiManager,
                  private val customerDao: CustomerDao,
                  private val favoriteProductDao: FavoriteProductDao) {

    fun getCustomerName(): Single<String> {
        return customerDao.getCustomerData()
                .flatMap { Single.just(it.firstName) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchLatestProduct(): Observable<List<ProductList>> {
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

    fun fetchMainMenu(): Observable<MutableList<MainMenu>> {
        val menus = mutableListOf<MainMenu>()
        menus.add(MainMenu("Semua Produk", R.drawable.icon_main_product, android.R.color.holo_green_light))
        menus.add(MainMenu("Cek Ongkir", R.drawable.icon_shipping_cost, android.R.color.holo_orange_light))
        menus.add(MainMenu("Info Toko", R.drawable.icon_store, android.R.color.holo_blue_light))
        return Observable.just(menus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchFavoriteProduct(): Flowable<List<FavoriteProduct>> {
        return favoriteProductDao.getFavoriteProductData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getCurrentThreadInfo(): String {
        return Thread.currentThread().name + "(" + Thread.currentThread().id + ")"
    }
}