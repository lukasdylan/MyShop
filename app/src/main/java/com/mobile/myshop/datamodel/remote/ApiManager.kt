package com.mobile.myshop.datamodel.remote

import com.mobile.myshop.datamodel.response.LoginResponse
import com.mobile.myshop.datamodel.response.ProductListResponse
import com.mobile.myshop.datamodel.response.RegisterResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 4/26/2018.
 */
interface ApiManager {
    @POST("login.php")
    @FormUrlEncoded
    fun requestLogin(@Field("nohp") nohp: String,
                     @Field("password") password: String): Observable<LoginResponse>

    @POST("register.php")
    @FormUrlEncoded
    fun requestRegister(@Field("name") name: String,
                        @Field("phone_number") nohp: String,
                        @Field("password") password: String,
                        @Field("devid") deviceId: String): Observable<RegisterResponse>

    @POST("get_produk_detail.php")
    @FormUrlEncoded
    fun requestProductList(@Field("idpenjual") sellerId: String): Observable<ProductListResponse>
}