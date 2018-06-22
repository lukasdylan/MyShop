package com.mobile.myshop.utils

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.androidadvance.topsnackbar.TSnackbar
import com.mobile.myshop.R
import com.mobile.myshop.databinding.LayoutDialogBinding
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/26/2018.
 */
object UiUtils {
    const val DIALOG_LOADING_TYPE = 0
    const val DIALOG_CONFIRMATION_TYPE = 1
    const val DIALOG_SUCCESS_TYPE = 2

    interface DialogCallback {
        fun onConfirm(dialogType: Int)
        fun onCancel()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun hideKeyboard(view: View, context: Context?) {
        context?.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showSnackBar(view: View, message: String, @ColorRes backColor: Int?, @ColorRes textColor: Int?) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        val snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackBarTextView.setTextAppearance(R.style.SnackBarTextAppearance)
        } else{
            TextViewCompat.setTextAppearance(snackBarTextView, R.style.SnackBarTextAppearance)
        }
        backColor?.apply {
            snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, this))
        }
        textColor?.apply {
            snackBarTextView.setTextColor(ContextCompat.getColor(view.context, this))
        }
        snackbar.show()
    }

    fun showActionSnackBar(view: View,
                           message: String,
                           @ColorRes backColor: Int?,
                           @ColorRes textColor: Int?,
                           actionText: String,
                           publishAction: PublishSubject<Boolean>?){
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        val snackBarView = snackbar.view
        val snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackBarTextView.setTextAppearance(R.style.SnackBarTextAppearance)
        } else{
            TextViewCompat.setTextAppearance(snackBarTextView, R.style.SnackBarTextAppearance)
        }
        backColor?.apply {
            snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, this))
        }
        textColor?.apply {
            snackBarTextView.setTextColor(ContextCompat.getColor(view.context, this))
            snackbar.setActionTextColor(ContextCompat.getColor(view.context, this))
        }
        snackbar.setAction(actionText, {
            publishAction?.onNext(true)
        })
        snackbar.show()
    }

    fun showTopSnackBar(view: View, message: String, @ColorRes backColor: Int?, @ColorRes textColor: Int?) {
        TSnackbar.make(view, message, TSnackbar.LENGTH_LONG).run {
            val snackBarTextView = this.view.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                snackBarTextView.setTextAppearance(R.style.SnackBarTextAppearance)
            } else{
                TextViewCompat.setTextAppearance(snackBarTextView, R.style.SnackBarTextAppearance)
            }
            backColor?.apply {
                this@run.view.setBackgroundColor(ContextCompat.getColor(view.context, this))
            }
            textColor?.apply {
                snackBarTextView.setTextColor(ContextCompat.getColor(view.context, this))
            }
            show()
        }
    }

    fun showDialog(context: Context, message: String, dialogType: Int, dialogCallback: DialogCallback?): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.Theme_Dialog)
        val layoutInflater = LayoutInflater.from(context)
        val layoutDialogBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_dialog, null, false) as LayoutDialogBinding
        alertDialogBuilder.apply {
            setCancelable(false)
            setView(layoutDialogBinding.root)
            when (dialogType) {
                DIALOG_LOADING_TYPE -> {
                    layoutDialogBinding.apply {
                        loadingView.apply {
                            visibility = View.VISIBLE
                            setAnimation("loading.json", LottieAnimationView.CacheStrategy.Strong)
                            repeatCount = LottieDrawable.INFINITE
                            playAnimation()
                        }
                        messageTV.text = message
                        buttonLayout.visibility = View.GONE
                    }
                }
                DIALOG_CONFIRMATION_TYPE -> {
                    layoutDialogBinding.apply {
                        loadingView.visibility = View.GONE
                        messageTV.text = message
                        buttonLayout.visibility = View.VISIBLE
                        confirmButton.visibility = View.VISIBLE
                        cancelButton.visibility = View.VISIBLE
                        confirmButton.setOnClickListener { dialogCallback?.onConfirm(DIALOG_CONFIRMATION_TYPE) }
                        cancelButton.setOnClickListener { dialogCallback?.onCancel() }
                    }
                }
                DIALOG_SUCCESS_TYPE -> {
                    layoutDialogBinding.apply {
                        loadingView.apply {
                            visibility = View.VISIBLE
                            setAnimation("success.json", LottieAnimationView.CacheStrategy.Strong)
                            repeatCount = 1
                            playAnimation()
                        }
                        messageTV.text = message
                        buttonLayout.visibility = View.VISIBLE
                        confirmButton.setOnClickListener { dialogCallback?.onConfirm(DIALOG_SUCCESS_TYPE) }
                    }
                }
            }
        }
        return alertDialogBuilder.create().apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
            show()
        }
    }
}