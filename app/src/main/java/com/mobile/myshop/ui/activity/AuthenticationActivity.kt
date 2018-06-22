package com.mobile.myshop.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.mobile.myshop.R
import com.mobile.myshop.ui.fragment.LoginFragment
import com.mobile.myshop.ui.fragment.RegistrationFragment
import com.mobile.myshop.utils.UiUtils
import com.mobile.myshop.viewmodel.AuthenticationViewModel
import com.mobile.myshop.viewmodel.AuthenticationViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_authentication.*
import javax.inject.Inject


class AuthenticationActivity : BaseDaggerActivity(), LoginFragment.LoginFragmentCallback, HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var authenticationViewModelFactory: AuthenticationViewModelFactory

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authenticationViewModel = ViewModelProviders.of(this, authenticationViewModelFactory).get(AuthenticationViewModel::class.java)
        authenticationViewModel.apply {
            userFound.observe(this@AuthenticationActivity, Observer {
                it?.let { found ->
                    if (found) {
                        openActivity(MainActivity(), Bundle.EMPTY, Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        overridePendingTransition(0, 0)
                        finish()
                    } else {
                        setContentView(R.layout.activity_authentication)
                        openFragmentNoAnimation(container.id, LoginFragment())
                    }
                }
            })

            isAlreadyLoggedIn()
        }
    }

    override fun toRegisterFragment() {
        openFragment(container.id, RegistrationFragment())
    }

    override fun toMainActivity(bundle: Bundle?) {
        openActivity(MainActivity(), bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            UiUtils.hideKeyboard(container, this)
            onBackPressed()
        }
        return true
    }
}
