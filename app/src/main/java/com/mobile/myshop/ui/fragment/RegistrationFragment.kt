package com.mobile.myshop.ui.fragment


import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.FragmentRegistrationBinding
import com.mobile.myshop.ui.activity.AuthenticationActivity
import com.mobile.myshop.utils.UiUtils
import com.mobile.myshop.viewmodel.RegisterViewModel
import com.mobile.myshop.viewmodel.RegisterViewModelFactory
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : BaseFragment(), UiUtils.DialogCallback {

    @Inject
    lateinit var registerViewModelFactory: RegisterViewModelFactory

    private var fragmentRegistrationBinding: FragmentRegistrationBinding? = null
    private var registerViewModel: RegisterViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerViewModel = ViewModelProviders.of(this, registerViewModelFactory).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentRegistrationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false) as FragmentRegistrationBinding
        return fragmentRegistrationBinding?.let {
            it.viewModel = registerViewModel
            (activity as AuthenticationActivity).setSupportActionBar(it.toolbar)
            (activity as AuthenticationActivity).supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = ""
            }
            it.appBarLayout.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    outlineProvider = null
                }
            }
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModel?.let {
            it.dialogState.observe(this, Observer {
                it?.apply {
                    when (first) {
                        RegisterViewModel.CONFIRMATION_STATE -> {
                            showDialog(UiUtils.DIALOG_CONFIRMATION_TYPE, second, this@RegistrationFragment)
                        }
                        RegisterViewModel.LOADING_STATE -> {
                            hideDialog()
                            showDialog(UiUtils.DIALOG_LOADING_TYPE, second, null)
                        }
                        RegisterViewModel.SUCCESS_STATE -> {
                            hideDialog()
                            showDialog(UiUtils.DIALOG_SUCCESS_TYPE, second, this@RegistrationFragment)
                        }
                        RegisterViewModel.FAILED_STATE -> {
                            hideDialog()
                            fragmentRegistrationBinding?.let {
                                UiUtils.showTopSnackBar(it.coordinatorLayout, second, android.R.color.holo_red_light, android.R.color.white)
                            }
                        }
                    }
                }
            })

            it.errorMessage.observe(this, Observer { message ->
                fragmentRegistrationBinding?.let {
                    message?.apply {
                        UiUtils.showTopSnackBar(it.coordinatorLayout, this, android.R.color.holo_red_light, android.R.color.white)
                    }
                }
            })

            it.hideKeyboard.observe(this, Observer {
                fragmentRegistrationBinding?.root?.let { UiUtils.hideKeyboard(it, context) }
            })
        }
    }

    @SuppressLint("HardwareIds")
    override fun onConfirm(dialogType: Int) {
        registerViewModel?.let {
            when (it.currentDialogState) {
                RegisterViewModel.CONFIRMATION_STATE -> {
                    val deviceId = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
                    it.onRegisterRequest(deviceId)
                }
                RegisterViewModel.SUCCESS_STATE -> {
                    hideDialog()
                    (activity as AuthenticationActivity).onBackPressed()
                }
            }
        }
    }

    override fun onCancel() {
        hideDialog()
    }
}
