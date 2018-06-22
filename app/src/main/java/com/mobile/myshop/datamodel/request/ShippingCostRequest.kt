package com.mobile.myshop.datamodel.request

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/1/2018.
 */
class ShippingCostRequest {
    var provinceCode: String = ""
    var cityCode: String = ""
    var weightTotal: Int = 0
    var weightTotalDouble: Double = 0.0
    var courierName: String = ""
}