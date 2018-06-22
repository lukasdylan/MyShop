package com.mobile.myshop.core.module

import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.usecase.ProductListUseCase
import com.mobile.myshop.viewmodel.ProductListViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/9/2018.
 */
@Module
class ProductListModule {
    @Provides
    fun provideUseCase(apiManager: ApiManager): ProductListUseCase = ProductListUseCase(apiManager)

    @Provides
    fun provideViewModelFactory(productListUseCase: ProductListUseCase): ProductListViewModelFactory
            = ProductListViewModelFactory(productListUseCase)
}