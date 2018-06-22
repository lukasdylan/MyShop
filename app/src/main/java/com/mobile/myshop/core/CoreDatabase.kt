package com.mobile.myshop.core

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mobile.myshop.datamodel.constant.AppConstant
import com.mobile.myshop.datamodel.entity.Customer
import com.mobile.myshop.datamodel.entity.FavoriteProduct
import com.mobile.myshop.datamodel.repository.CustomerDao
import com.mobile.myshop.datamodel.repository.FavoriteProductDao

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/1/2018.
 */
@Database(version = AppConstant.DATABASE_VERSION, entities = [Customer::class, FavoriteProduct::class], exportSchema = false)
abstract class CoreDatabase: RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun favoriteProductDao(): FavoriteProductDao
}