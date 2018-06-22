package com.mobile.myshop.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import com.mobile.myshop.R
import com.mobile.myshop.datamodel.entity.FavoriteProduct
import com.mobile.myshop.datamodel.entity.MainMenu
import com.mobile.myshop.datamodel.response.ProductList
import com.mobile.myshop.datamodel.usecase.HomeUseCase
import com.mobile.myshop.utils.ResourceProvider
import com.mobile.myshop.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.io.EOFException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*


/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 5/21/2018.
 */

class HomeViewModel(private val homeUseCase: HomeUseCase, private val resourceProvider: ResourceProvider) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var latestProductResult = MutableLiveData<Pair<Int, List<ProductList>>>()
    var favoriteProductResult = MutableLiveData<Pair<Int, List<FavoriteProduct>>>()
    var mainMenuList = MutableLiveData<List<MainMenu>>()
    var greetings = ObservableField<String>()
    var customerName = ObservableField<String>()
    var viewMoreLatestProduct = ObservableInt(View.INVISIBLE)
    var viewMoreFavoriteProduct = ObservableInt(View.INVISIBLE)
    var errorMessage = SingleLiveEvent<String>()
    var viewMoreNavigation = SingleLiveEvent<Int>()

    fun onViewMoreLatestProduct() {
        viewMoreNavigation.value = VIEW_MORE_LATEST_PRODUCT
    }

    fun onViewMoreFavoriteProduct() {
        viewMoreNavigation.value = VIEW_MORE_FAVORITE_PRODUCT
    }

    fun loadGreetings() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..10 -> greetings.set("Selamat Pagi,")
            in 11..14 -> greetings.set("Selamat Siang,")
            in 15..18 -> greetings.set("Selamat Sore,")
            else -> greetings.set("Selamat Malam,")
        }
    }

    fun loadCustomerName() {
        compositeDisposable.add(
                homeUseCase.getCustomerName()
                        .subscribe({
                            customerName.set(it)
                        }, {
                            customerName.set("Lukas Dylan")
                        })
        )
    }

    fun loadMainMenu() {
        compositeDisposable.add(
                homeUseCase.fetchMainMenu()
                        .subscribe({ mainMenuList.value = it }))
    }

    fun loadLatestProduct() {
        compositeDisposable.add(
                homeUseCase.fetchLatestProduct()
                        .doOnSubscribe { latestProductResult.postValue(Pair(1, mutableListOf())) }
                        .subscribe({
                            latestProductResult.value = Pair(0, it)
                            if (it.size >= 5) {
                                viewMoreLatestProduct.set(View.VISIBLE)
                            } else {
                                viewMoreLatestProduct.set(View.INVISIBLE)
                            }
                        }, {
                            latestProductResult.value = Pair(2, mutableListOf())
                            if (it is SocketException || it is SocketTimeoutException) {
                                errorMessage.value = resourceProvider.getString(R.string.msg_error_connection)
                            } else if (it is UnknownHostException || it is EOFException) {
                                errorMessage.value = "Ada masalah dengan server. Silahkan coba kembali"
                            } else {
                                errorMessage.value = it.localizedMessage
                            }
                        })
        )
    }

    fun loadFavoriteProducts() {
        compositeDisposable.add(
                homeUseCase.fetchFavoriteProduct()
                        .subscribe({
                            if (it.isEmpty()) {
                                favoriteProductResult.value = Pair(2, it)
                            } else {
                                if (it.size >= 5) {
                                    viewMoreFavoriteProduct.set(View.VISIBLE)
                                } else {
                                    viewMoreFavoriteProduct.set(View.INVISIBLE)
                                }
                                favoriteProductResult.value = Pair(1, it)
                            }
                        })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun getCurrentThreadInfo(): String {
        return Thread.currentThread().name + "(" + Thread.currentThread().id + ")"
    }

    companion object {
        const val VIEW_MORE_LATEST_PRODUCT = 0
        const val VIEW_MORE_FAVORITE_PRODUCT = 1
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val homeUseCase: HomeUseCase, private val resourceProvider: ResourceProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(homeUseCase, resourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}