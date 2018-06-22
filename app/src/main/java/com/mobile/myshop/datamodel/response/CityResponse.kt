package com.mobile.myshop.datamodel.response

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/30/2018.
 */
data class CityResponse constructor(@SerializedName("rajaongkir")
                                    val rajaongkir: RajaOngkirCity?)

data class RajaOngkirCity constructor(@SerializedName("status")
                                      val status: TrackingStatusResponse?,
                                      @SerializedName("results")
                                      val results: List<CityResult> = ArrayList())

data class CityResult constructor(@SerializedName("city_id")
                                  val cityId: String?,
                                  @SerializedName("province_id")
                                  val provinceId: String?,
                                  @SerializedName("province")
                                  val province: String?,
                                  @SerializedName("type")
                                  val type: String?,
                                  @SerializedName("city_name")
                                  val cityName: String?,
                                  @SerializedName("postal_code")
                                  val postalCode: String?)