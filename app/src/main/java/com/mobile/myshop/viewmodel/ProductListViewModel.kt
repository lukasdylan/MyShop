package com.mobile.myshop.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.view.View.*
import com.mobile.myshop.datamodel.response.ProductList
import com.mobile.myshop.datamodel.usecase.ProductListUseCase
import com.mobile.myshop.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/8/2018.
 */

class ProductListViewModel(private val productListUseCase: ProductListUseCase) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var loadingLayout = ObservableInt(VISIBLE)
    var loadMoreLayout = ObservableInt(GONE)
    var searchLayout = ObservableInt(INVISIBLE)
    var resetSearchLayout = ObservableInt(GONE)
    var productListResult = MutableLiveData<List<ProductList>>()
    var errorMessage = SingleLiveEvent<String>()
    var errorLayout = ObservableInt(GONE)
    var fabLayout = ObservableBoolean(false)
    var fabEvent = SingleLiveEvent<Void>()

    init {
        fabLayout.set(false)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun onFabClicked() {
        fabEvent.call()
    }

    fun loadProductList() {
        compositeDisposable.add(
                productListUseCase.fetchProductList()
                        .doOnSubscribe { loadingLayout.set(VISIBLE) }
                        .doAfterTerminate { loadingLayout.set(GONE) }
                        .subscribe({
                            productListResult.value = it
                            searchLayout.set(VISIBLE)
                            fabLayout.set(true)
                        }, {
                            errorMessage.value = it.localizedMessage
                        })
        )
    }
}

@Suppress("UNCHECKED_CAST")
class ProductListViewModelFactory(private val productListUseCase: ProductListUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            return ProductListViewModel(productListUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}