package com.mobile.myshop.ui.activity

import android.os.Bundle
import dagger.android.AndroidInjection

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 5/19/2018.
 */
abstract class BaseDaggerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}