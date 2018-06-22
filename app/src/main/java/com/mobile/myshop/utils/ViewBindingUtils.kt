package com.mobile.myshop.utils

import android.databinding.BindingAdapter
import android.support.annotation.DrawableRes
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.mobile.myshop.R
import com.mobile.myshop.core.GlideApp
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/5/2018.
 */
object ViewBindingUtils {
    @JvmStatic
    @BindingAdapter("setErrorInputText")
    fun TextInputLayout.setErrorInputText(message: String?) {
        if (message != null) {
            isErrorEnabled = true
            error = message
        } else {
            isErrorEnabled = false
            error = null
        }
    }

    @JvmStatic
    @BindingAdapter("setPhotoByUrl")
    fun ImageView.setPhotoByUrl(photoUrl: String?) {
        val requestOptions = RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.color.lightGray)
                .error(R.color.lightGray)
        GlideApp.with(context)
                .load(photoUrl)
                .apply(requestOptions)
                .transition(withCrossFade())
                .into(this)
    }

    @JvmStatic
    @BindingAdapter("setImageFromResource")
    fun ImageView.setImageFromResource(@DrawableRes resource: Int) {
        val requestOptions = RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        GlideApp.with(context)
                .load(ContextCompat.getDrawable(context, resource))
                .apply(requestOptions)
                .transition(withCrossFade())
                .into(this)
    }

    @JvmStatic
    @BindingAdapter("setTextPrice")
    fun TextView.setTextPrice(price: Double) {
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        val symbols = formatter.decimalFormatSymbols
        symbols.groupingSeparator = '.'
        symbols.decimalSeparator = ','
        formatter.decimalFormatSymbols = symbols
        text = String.format("Rp %s", formatter.format(price))
    }

    @JvmStatic
    @BindingAdapter("isShown")
    fun FloatingActionButton.isShown(show: Boolean) {
        visibility = if (show) {
            show()
            View.VISIBLE
        } else {
            hide()
            View.INVISIBLE
        }
    }
}