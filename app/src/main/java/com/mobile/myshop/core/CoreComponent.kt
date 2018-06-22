package com.mobile.myshop.core

import com.mobile.myshop.core.module.CoreAppModule
import com.mobile.myshop.core.module.CoreBuilderModule
import com.mobile.myshop.core.module.CoreNetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/26/2018.
 */
@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class),
    (CoreNetworkModule::class),
    (CoreBuilderModule::class),
    (CoreAppModule::class)])
interface CoreComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(coreApplication: CoreApplication): Builder

        fun build(): CoreComponent
    }

    fun inject(coreApplication: CoreApplication)
}