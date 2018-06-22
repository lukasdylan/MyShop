package com.mobile.myshop.core.module

import android.arch.persistence.room.Room
import android.content.Context
import android.content.res.Resources
import com.mobile.myshop.core.CoreApplication
import com.mobile.myshop.core.CoreDatabase
import com.mobile.myshop.datamodel.constant.AppConstant
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/1/2018.
 */
@Module
class CoreAppModule {
    @Provides
    @Singleton
    fun provideContext(coreApplication: CoreApplication): Context {
        return coreApplication.applicationContext
    }

    @Provides
    @Singleton
    fun provideResource(context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): CoreDatabase{
        return Room.databaseBuilder(context, CoreDatabase::class.java, AppConstant.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideRefWatcher(coreApplication: CoreApplication): RefWatcher {
        return LeakCanary.install(coreApplication)
    }
}