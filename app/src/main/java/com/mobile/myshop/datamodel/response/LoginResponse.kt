package com.mobile.myshop.datamodel.response

import com.google.gson.annotations.SerializedName
import com.mobile.myshop.datamodel.entity.Customer

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/1/2018.
 */

data class LoginResponse constructor(@SerializedName("statusCode")
                                     val statusCode: Int?,
                                     @SerializedName("message")
                                     val message: String?,
                                     @SerializedName("pembeli")
                                     val customer: List<Customer>?)