package com.mobile.myshop.datamodel.repository

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mobile.myshop.datamodel.entity.Customer
import io.reactivex.Single

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 5/1/2018.
 */
@Dao
interface CustomerDao {
    @Query("SELECT * FROM Customer")
    fun getCustomerData(): Single<Customer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCustomerData(customer: Customer)
}