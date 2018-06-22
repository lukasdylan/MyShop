package com.mobile.myshop.datamodel.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/24/2018.
 */
data class ProductListResponse constructor(@SerializedName("barang")
                                           val productList: List<ProductList>?)

@Parcelize
data class ProductList constructor(@SerializedName("kode")
                                   val productCode: String?,
                                   @SerializedName("nama")
                                   val productName: String?,
                                   @SerializedName("harga")
                                   val productPrice: String?,
                                   @SerializedName("deskripsi")
                                   val productDesc: String?,
                                   @SerializedName("foto")
                                   val productPhotos: List<ProductPhotos>?) : Parcelable

@Parcelize
data class ProductPhotos constructor(@SerializedName("linkfoto")
                                     val photoName: String?) : Parcelable