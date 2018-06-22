package com.mobile.myshop.core.module

import com.mobile.myshop.core.CoreDatabase
import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.repository.CustomerDao
import com.mobile.myshop.datamodel.repository.FavoriteProductDao
import com.mobile.myshop.datamodel.usecase.HomeUseCase
import com.mobile.myshop.utils.ResourceProvider
import com.mobile.myshop.viewmodel.HomeViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 5/28/2018.
 */
@Module
class HomeModule {

    @Provides
    fun provideCustomerDao(coreDatabase: CoreDatabase): CustomerDao = coreDatabase.customerDao()

    @Provides
    fun provideFavoriteProductDao(coreDatabase: CoreDatabase): FavoriteProductDao = coreDatabase.favoriteProductDao()

    @Provides
    fun provideUseCase(apiManager: ApiManager,
                       customerDao: CustomerDao,
                       favoriteProductDao: FavoriteProductDao): HomeUseCase = HomeUseCase(apiManager, customerDao, favoriteProductDao)

    @Provides
    fun provideViewModelFactory(homeUseCase: HomeUseCase,
                                resourceProvider: ResourceProvider): HomeViewModelFactory = HomeViewModelFactory(homeUseCase, resourceProvider)
}