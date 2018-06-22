package com.mobile.myshop.core.module

import com.mobile.myshop.datamodel.remote.RajaOngkirApiManager
import com.mobile.myshop.datamodel.usecase.AdministrativeUseCase
import com.mobile.myshop.viewmodel.AdministrativeViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/2/2018.
 */
@Module
class AdministrativeModule {

    @Provides
    fun provideUseCase(rajaOngkirApiManager: RajaOngkirApiManager): AdministrativeUseCase = AdministrativeUseCase(rajaOngkirApiManager)

    @Provides
    fun provideViewModelFactory(administrativeUseCase: AdministrativeUseCase): AdministrativeViewModelFactory = AdministrativeViewModelFactory(administrativeUseCase)
}