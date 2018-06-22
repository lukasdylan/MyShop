package com.mobile.myshop.core.module

import com.mobile.myshop.core.CoreDatabase
import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.repository.CustomerDao
import com.mobile.myshop.datamodel.usecase.LoginUseCase
import com.mobile.myshop.utils.ResourceProvider
import com.mobile.myshop.viewmodel.LoginViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 4/30/2018.
 */
@Module
class LoginModule {

    @Provides
    fun provideCustomerDao(coreDatabase: CoreDatabase): CustomerDao {
        return coreDatabase.customerDao()
    }

    @Provides
    fun provideUseCase(apiManager: ApiManager, customerDao: CustomerDao): LoginUseCase {
        return LoginUseCase(apiManager, customerDao)
    }

    @Provides
    fun provideViewModelFactory(loginUseCase: LoginUseCase, resourceProvider: ResourceProvider): LoginViewModelFactory {
        return LoginViewModelFactory(loginUseCase, resourceProvider)
    }
}