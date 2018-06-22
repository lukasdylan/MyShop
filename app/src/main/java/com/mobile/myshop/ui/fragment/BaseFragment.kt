package com.mobile.myshop.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.mobile.myshop.BuildConfig
import com.mobile.myshop.utils.UiUtils
import com.squareup.leakcanary.RefWatcher
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 4/28/2018.
 */
abstract class BaseFragment : Fragment() {

    private var alertDialog: AlertDialog? = null

    @Inject
    lateinit var refWatcher: RefWatcher

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG) {
            refWatcher.watch(this)
        }
    }

    private fun isFragmentAlive(): Boolean {
        return context != null && isVisible
    }

    protected fun openActivity(activity: AppCompatActivity, bundle: Bundle?, flag: Int?) {
        if (isFragmentAlive()) {
            val intent = Intent(context, activity.javaClass)
            bundle?.let {
                intent.putExtras(it)
            }
            flag?.let {
                intent.addFlags(it)
            }
            startActivity(intent)
        }
    }

    protected fun openActivityForResult(activity: AppCompatActivity, requestCode: Int, bundle: Bundle?, flag: Int?) {
        if (isFragmentAlive()) {
            val intent = Intent(context, activity.javaClass)
            bundle?.let {
                intent.putExtras(it)
            }
            flag?.let {
                intent.addFlags(it)
            }
            startActivityForResult(intent, requestCode)
        }
    }

    protected fun showDialog(dialogType: Int, message: String, dialogCallback: UiUtils.DialogCallback?) {
        context?.let {
            alertDialog = UiUtils.showDialog(it, message, dialogType, dialogCallback)
        }
    }

    protected fun hideDialog() {
        alertDialog?.dismiss()
    }
}