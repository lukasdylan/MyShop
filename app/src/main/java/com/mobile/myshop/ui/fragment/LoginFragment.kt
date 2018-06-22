package com.mobile.myshop.ui.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.FragmentLoginBinding
import com.mobile.myshop.utils.UiUtils
import com.mobile.myshop.viewmodel.LoginViewModel
import com.mobile.myshop.viewmodel.LoginViewModelFactory
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment(){

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    private var loginViewModel: LoginViewModel? = null
    private var fragmentLoginBinding: FragmentLoginBinding? = null
    private var loginFragmentCallback: LoginFragmentCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LoginFragmentCallback) {
            try {
                loginFragmentCallback = context
            } catch (ex: ClassCastException) {
                throw ClassCastException(context.toString() + "must implement LoginFragmentCallback")
            }
        }
    }

    override fun onDestroy() {
        loginFragmentCallback = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProviders.of(this, loginViewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false) as FragmentLoginBinding
        return fragmentLoginBinding?.let {
            it.viewModel = loginViewModel
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel?.let {
            it.errorMessage.observe(this, Observer { messageState ->
                messageState?.let { message ->
                    fragmentLoginBinding?.let {
                        UiUtils.showSnackBar(it.coordinatorLayout, message, android.R.color.holo_red_light, android.R.color.white)
                    }
                }
            })

            it.loading.observe(this, Observer { loadingState ->
                loadingState?.apply {
                    if (this) {
                        showDialog(UiUtils.DIALOG_LOADING_TYPE, "Mohon Tunggu Sebentar...", null)
                    } else {
                        hideDialog()
                    }
                }
            })

            it.screenNavigation.observe(this, Observer { navigationState ->
                navigationState?.let {
                    if (it.first == LoginViewModel.MAIN_PAGE) {
                        loginFragmentCallback?.toMainActivity(it.second)
                    } else {
                        loginFragmentCallback?.toRegisterFragment()
                    }
                }
            })

            it.hideKeyboard.observe(this, Observer {
                fragmentLoginBinding?.root?.let { UiUtils.hideKeyboard(it, context) }
            })
        }
    }

    interface LoginFragmentCallback {
        fun toRegisterFragment()
        fun toMainActivity(bundle: Bundle?)
    }
}
