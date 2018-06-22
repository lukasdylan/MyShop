package com.mobile.myshop.datamodel.repository

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mobile.myshop.datamodel.entity.FavoriteProduct
import io.reactivex.Flowable

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/1/2018.
 */
@Dao
interface FavoriteProductDao {
    @Query("SELECT * FROM FavoriteProduct")
    fun getFavoriteProductData(): Flowable<List<FavoriteProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFavoriteProduct(favoriteProduct: FavoriteProduct)
}