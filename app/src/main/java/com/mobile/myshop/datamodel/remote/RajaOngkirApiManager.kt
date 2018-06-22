package com.mobile.myshop.datamodel.remote

import com.mobile.myshop.datamodel.response.CityResponse
import com.mobile.myshop.datamodel.response.ProvinceResponse
import com.mobile.myshop.datamodel.response.ShippingCostResponse
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/26/2018.
 */
interface RajaOngkirApiManager {
    @GET("province")
    fun requestListProvince(@Header("key") apiKey: String): Observable<ProvinceResponse>

    @GET("city")
    fun requestListCityByProvince(@Header("key") apiKey: String, @Query("province") provinceCode: String): Observable<CityResponse>

    @FormUrlEncoded
    @POST("cost")
    fun requestShippingCost(@Header("key") apiKey: String,
                            @Field("origin") originCode: String,
                            @Field("destination") destinationCode: String,
                            @Field("weight") weight: Int,
                            @Field("courier") courierName: String): Observable<ShippingCostResponse>
}