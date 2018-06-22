package com.mobile.myshop.datamodel.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/1/2018.
 */
@Entity
class Customer {
    @PrimaryKey
    @SerializedName("idpembeli")
    var customerId: String = ""
    @SerializedName("namadpn")
    var firstName: String = ""
    @SerializedName("namablkg")
    var lastName: String = ""
    @SerializedName("alamat")
    var address: String = ""
    @SerializedName("nohp")
    var phoneNumber: String = ""
    @SerializedName("bbm")
    var bbmPin: String = ""
    @SerializedName("tgljoin")
    var joinDate: String = ""
    @SerializedName("devid")
    var deviceId: String = ""
    @SerializedName("status")
    var statusAccount: String = ""
}