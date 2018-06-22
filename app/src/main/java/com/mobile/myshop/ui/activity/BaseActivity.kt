package com.mobile.myshop.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.mobile.myshop.R
import com.mobile.myshop.utils.UiUtils

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/28/2018.
 */
abstract class BaseActivity : AppCompatActivity() {

    private var alertDialog: AlertDialog? = null

    protected fun openActivity(activity: AppCompatActivity, bundle: Bundle?, flag: Int?) {
        val intent = Intent(this, activity.javaClass)
        bundle?.let {
            intent.putExtras(it)
        }
        flag?.let {
            intent.addFlags(it)
        }
        startActivity(intent)
    }

    protected fun openActivityForResult(activity: AppCompatActivity, requestCode: Int, bundle: Bundle?, flag: Int?) {
        val intent = Intent(this, activity.javaClass)
        bundle?.let {
            intent.putExtras(it)
        }
        flag?.let {
            intent.addFlags(it)
        }
        startActivityForResult(intent, requestCode)
    }

    protected fun openFragment(containerViewId: Int, fragment: Fragment) {
        supportFragmentManager?.apply {
            beginTransaction()
                    .setCustomAnimations(R.anim.from_right_in, R.anim.from_left_out, R.anim.from_left_in, R.anim.from_right_out)
                    .replace(containerViewId, fragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    protected fun openFragmentNoAnimation(containerViewId: Int, fragment: Fragment) {
        supportFragmentManager?.apply {
            beginTransaction()
                    .replace(containerViewId, fragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager?.apply {
            if (backStackEntryCount > 1) {
                popBackStack()
            } else {
                finish()
            }
        }
    }

    protected fun showDialog(dialogType: Int, message: String, dialogCallback: UiUtils.DialogCallback?) {
        alertDialog = UiUtils.showDialog(this, message, dialogType, dialogCallback)
    }

    protected fun hideDialog() {
        alertDialog?.dismiss()
    }
}