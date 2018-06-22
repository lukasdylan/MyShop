package com.mobile.myshop.datamodel.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.mobile.myshop.utils.DateConverter
import com.mobile.myshop.utils.StringListConverter
import java.util.*

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/1/2018.
 */
@Entity
class FavoriteProduct {
    @PrimaryKey
    var productCode: String = ""
    var productName: String = ""
    var productPrice: String = ""
    var productDesc: String = ""
    @TypeConverters(StringListConverter::class)
    var productPhotos: List<String> = mutableListOf()
    @TypeConverters(DateConverter::class)
    var lastUpdated: Date = Date()
}
