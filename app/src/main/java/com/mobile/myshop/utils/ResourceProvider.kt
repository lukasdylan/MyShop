package com.mobile.myshop.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import javax.inject.Inject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/24/2018.
 */
class ResourceProvider @Inject constructor(private val resources: Resources) {

    fun getString(resId: Int): String {
        return resources.getString(resId)
    }

    fun getStringArray(resId: Int): Array<String> {
        return resources.getStringArray(resId)
    }

    @Suppress("DEPRECATION")
    fun getDrawable(resId: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resources.getDrawable(resId, null)
        } else {
            resources.getDrawable(resId)
        }
    }
}