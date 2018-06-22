package com.mobile.myshop.core.module

import com.mobile.myshop.datamodel.remote.RajaOngkirApiManager
import com.mobile.myshop.datamodel.usecase.ShippingCostUseCase
import com.mobile.myshop.viewmodel.ShippingCostViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/1/2018.
 */
@Module
class ShippingCostModule {
    @Provides
    fun provideUseCase(rajaOngkirApiManager: RajaOngkirApiManager): ShippingCostUseCase = ShippingCostUseCase(rajaOngkirApiManager)

    @Provides
    fun provideViewModelFactory(shippingCostUseCase: ShippingCostUseCase): ShippingCostViewModelFactory = ShippingCostViewModelFactory(shippingCostUseCase)
}