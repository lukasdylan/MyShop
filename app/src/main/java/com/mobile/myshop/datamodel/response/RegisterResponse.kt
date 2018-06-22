package com.mobile.myshop.datamodel.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/3/2018.
 */
data class RegisterResponse constructor(@SerializedName("success")
                                        val statusCode: Int?,
                                        @SerializedName("message")
                                        val message: String?)