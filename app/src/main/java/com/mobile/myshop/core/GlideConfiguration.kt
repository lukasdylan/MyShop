package com.mobile.myshop.core

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.mobile.myshop.BuildConfig
import java.io.InputStream


/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/11/2018.
 */
@GlideModule
class GlideConfiguration : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.apply {
            if (BuildConfig.DEBUG) {
                setLogLevel(Log.DEBUG)
            }
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
    }
}