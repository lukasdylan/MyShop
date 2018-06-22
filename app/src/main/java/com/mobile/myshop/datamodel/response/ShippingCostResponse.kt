package com.mobile.myshop.datamodel.response

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/30/2018.
 */

data class ShippingCostResponse constructor(@SerializedName("rajaongkir")
                                            val rajaongkir: RajaOngkirCost?)

data class RajaOngkirCost constructor(@SerializedName("status")
                                      val status: TrackingStatusResponse?,
                                      @SerializedName("results")
                                      val results: List<ShippingCostResult> = ArrayList())

data class ShippingCostResult constructor(@SerializedName("code")
                                          val code: String?,
                                          @SerializedName("name")
                                          val name: String?,
                                          @SerializedName("costs")
                                          val costs: List<CostResult> = ArrayList())

data class CostResult constructor(@SerializedName("service")
                                  val service: String?,
                                  @SerializedName("description")
                                  val description: String?,
                                  @SerializedName("cost")
                                  val cost: List<ServiceCostResult> = ArrayList())

data class ServiceCostResult constructor(@SerializedName("value")
                                         val value: Int?,
                                         @SerializedName("etd")
                                         val etd: String?,
                                         @SerializedName("note")
                                         val note: String?)