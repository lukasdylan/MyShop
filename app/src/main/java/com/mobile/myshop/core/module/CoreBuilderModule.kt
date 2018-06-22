package com.mobile.myshop.core.module

import com.mobile.myshop.ui.activity.*
import com.mobile.myshop.ui.fragment.HomeFragment
import com.mobile.myshop.ui.fragment.LoginFragment
import com.mobile.myshop.ui.fragment.RegistrationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/26/2018.
 */
@Module
abstract class CoreBuilderModule {

    @ContributesAndroidInjector(modules = [(AuthenticationModule::class)])
    internal abstract fun bindAuthenticationActivity(): AuthenticationActivity

    @ContributesAndroidInjector(modules = [(LoginModule::class)])
    internal abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [(RegistrationModule::class)])
    internal abstract fun bindRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [(HomeModule::class)])
    internal abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [(ShippingCostModule::class)])
    internal abstract fun bindShippingCostActivity(): ShippingCostActivity

    @ContributesAndroidInjector(modules = [(AdministrativeModule::class)])
    internal abstract fun bindAdministrativeActivity(): AdministrativeActivity

    @ContributesAndroidInjector(modules = [(ProductListModule::class)])
    internal abstract fun bindProductListActivity(): ProductListActivity
}