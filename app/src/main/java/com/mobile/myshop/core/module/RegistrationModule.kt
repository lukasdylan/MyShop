package com.mobile.myshop.core.module

import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.usecase.RegisterUseCase
import com.mobile.myshop.utils.ResourceProvider
import com.mobile.myshop.viewmodel.RegisterViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/5/2018.
 */
@Module
class RegistrationModule {
    @Provides
    fun provideUseCase(apiManager: ApiManager): RegisterUseCase {
        return RegisterUseCase(apiManager)
    }

    @Provides
    fun provideViewModelFactory(registerUseCase: RegisterUseCase, resourceProvider: ResourceProvider): RegisterViewModelFactory {
        return RegisterViewModelFactory(registerUseCase, resourceProvider)
    }
}