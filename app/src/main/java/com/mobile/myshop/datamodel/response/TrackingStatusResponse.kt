package com.mobile.myshop.datamodel.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/30/2018.
 */
data class TrackingStatusResponse constructor(@SerializedName("code")
                                              val code: Int?,
                                              @SerializedName("description")
                                              val description: String?)