package com.mobile.myshop.ui.activity

import android.animation.ObjectAnimator
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.ViewOutlineProvider
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ActivityShippingCostBinding
import com.mobile.myshop.ui.adapter.ShippingCostAdapter
import com.mobile.myshop.ui.fragment.CourierFragment
import com.mobile.myshop.viewmodel.ShippingCostViewModel
import com.mobile.myshop.viewmodel.ShippingCostViewModelFactory
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ShippingCostActivity : BaseDaggerActivity() {

    @Inject
    lateinit var shippingCostViewModelFactory: ShippingCostViewModelFactory

    private var shippingCostViewModel: ShippingCostViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shippingCostViewModel = ViewModelProviders.of(this, shippingCostViewModelFactory).get(ShippingCostViewModel::class.java)
        val shippingCostBinding = DataBindingUtil.setContentView(this, R.layout.activity_shipping_cost) as ActivityShippingCostBinding
        shippingCostBinding.apply {
            initView(this)
            initViewModel(this)
        }
    }

    private fun initViewModel(shippingCostBinding: ActivityShippingCostBinding) {
        shippingCostViewModel?.apply {
            navigation.observe(this@ShippingCostActivity, Observer {
                it?.let {
                    when (it.first) {
                        ShippingCostViewModel.PROVINCE_REQUEST, ShippingCostViewModel.CITY_REQUEST -> {
                            openActivityForResult(AdministrativeActivity(), it.first, it.second, null)
                        }
                        ShippingCostViewModel.COURIER_REQUEST -> {
                            CourierFragment().apply {
                                getListener()
                                        .debounce(250, TimeUnit.MILLISECONDS)
                                        .subscribe({
                                            dismiss()
                                            onSelectedCourier(it)
                                        })
                            }.show(supportFragmentManager, null)
                        }
                    }
                }
            })

            shippingCostResult.observe(this@ShippingCostActivity, Observer {
                it?.apply {
                    shippingCostBinding.shippingCostRV.adapter = ShippingCostAdapter(first, second)
                    shippingCostBinding.scrollView.post {
                        ObjectAnimator.ofInt(shippingCostBinding.scrollView, "scrollY",
                                shippingCostBinding.shippingCostRV.bottom).apply {
                            duration = 1000
                            startDelay = 150
                        }.start()
                    }
                }
            })
        }
    }

    private fun initView(shippingCostBinding: ActivityShippingCostBinding) {
        shippingCostBinding.apply {
            viewModel = shippingCostViewModel
            appBarLayout.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    outlineProvider = null
                }
            }
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowHomeEnabled(true)
                setDisplayHomeAsUpEnabled(true)
            }
            shippingCostRV.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(DividerItemDecoration(this@ShippingCostActivity, DividerItemDecoration.VERTICAL))
                isNestedScrollingEnabled = false
            }
            scrollView.apply {
                isSmoothScrollingEnabled = true
                viewTreeObserver.addOnScrollChangedListener {
                    val weakBigTitleTv = WeakReference<TextView>(bigTitleTv)
                    val weakToolbarTitle = WeakReference<TextView>(toolbarTitle)
                    val weakAppBar = WeakReference<AppBarLayout>(appBarLayout)
                    weakBigTitleTv.get()?.let {
                        if (scrollY >= it.bottom) {
                            weakToolbarTitle.get()?.alpha = 1f
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                weakAppBar.get()?.outlineProvider = ViewOutlineProvider.BOUNDS
                            }
                        } else {
                            weakToolbarTitle.get()?.alpha = 0f
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                weakAppBar.get()?.outlineProvider = null
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            shippingCostViewModel?.onAdministrativeDataResult(requestCode, data)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }
}
