package com.mobile.myshop.core.module

import com.mobile.myshop.core.CoreDatabase
import com.mobile.myshop.datamodel.repository.CustomerDao
import com.mobile.myshop.datamodel.usecase.AuthenticationUseCase
import com.mobile.myshop.viewmodel.AuthenticationViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/18/2018.
 */
@Module
class AuthenticationModule {

    @Provides
    fun provideCustomerDao(coreDatabase: CoreDatabase) = coreDatabase.customerDao()

    @Provides
    fun provideUseCase(customerDao: CustomerDao): AuthenticationUseCase = AuthenticationUseCase(customerDao)

    @Provides
    fun provideViewModelFactory(authenticationUseCase: AuthenticationUseCase): AuthenticationViewModelFactory = AuthenticationViewModelFactory(authenticationUseCase)
}