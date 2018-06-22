package com.mobile.myshop.datamodel.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/30/2018.
 */
data class ProvinceResponse constructor(@SerializedName("rajaongkir")
                                        val rajaongkir: RajaOngkirProvince?)

data class RajaOngkirProvince constructor(@SerializedName("status")
                                          val status: TrackingStatusResponse?,
                                          @SerializedName("results")
                                          val results: List<ProvinceResult> = ArrayList())

data class ProvinceResult constructor(@SerializedName("province_id")
                                      val provinceId: String?,
                                      @SerializedName("province")
                                      val province: String?)