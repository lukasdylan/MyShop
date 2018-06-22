package com.mobile.myshop.ui.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ActivityAdministrativeBinding
import com.mobile.myshop.ui.adapter.ProvinceAdapter
import com.mobile.myshop.ui.adapter.RegionAdapter
import com.mobile.myshop.utils.UiUtils
import com.mobile.myshop.viewmodel.AdministrativeViewModel
import com.mobile.myshop.viewmodel.AdministrativeViewModelFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AdministrativeActivity : BaseDaggerActivity() {

    @Inject
    lateinit var administrativeViewModelFactory: AdministrativeViewModelFactory

    private var intentData: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val administrativeViewModel = ViewModelProviders.of(this, administrativeViewModelFactory).get(AdministrativeViewModel::class.java)
        val administrativeBinding = DataBindingUtil.setContentView(this, R.layout.activity_administrative) as ActivityAdministrativeBinding
        intentData = intent
        initView(administrativeBinding)
        initViewModel(administrativeBinding, administrativeViewModel)
    }

    private fun initView(administrativeBinding: ActivityAdministrativeBinding) {
        administrativeBinding.apply {
            appBarLayout.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    outlineProvider = null
                }
            }
            toolbar.apply {
                setSupportActionBar(this)
                supportActionBar?.setDisplayShowHomeEnabled(true)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            administrativeRV.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@AdministrativeActivity)
                addItemDecoration(DividerItemDecoration(this@AdministrativeActivity, DividerItemDecoration.VERTICAL))
            }
            title = if (intentData?.getIntExtra("request_code", PROVINCE_REQUEST) == PROVINCE_REQUEST) {
                "Pilih Provinsi"
            } else {
                "Pilih Wilayah"
            }
        }
    }

    private fun initViewModel(administrativeBinding: ActivityAdministrativeBinding,
                              administrativeViewModel: AdministrativeViewModel) {
        administrativeBinding.viewModel = administrativeViewModel.apply {
            provinceList.observe(this@AdministrativeActivity, Observer {
                it?.let {
                    administrativeBinding.administrativeRV.adapter = ProvinceAdapter(it).apply {
                        getSelectedProvince()
                                .debounce(200, TimeUnit.MILLISECONDS)
                                .subscribe({
                                    val intent = Intent().apply {
                                        putExtra("province_code", it.provinceId?.toInt())
                                        putExtra("province_name", it.province)
                                    }
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                })
                    }
                }
            })

            regionList.observe(this@AdministrativeActivity, Observer {
                it?.let {
                    administrativeBinding.administrativeRV.adapter = RegionAdapter(it).apply {
                        getSelectedRegion()
                                .debounce(200, TimeUnit.MILLISECONDS)
                                .subscribe({
                                    val intent = Intent().apply {
                                        putExtra("city_code", it.cityId?.toInt())
                                        putExtra("city_name", it.type + " " + it.cityName)
                                    }
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                })
                    }
                }
            })

            errorMessage.observe(this@AdministrativeActivity, Observer {
                it?.let {
                    UiUtils.showSnackBar(administrativeBinding.coordinatorLayout, it,
                            android.R.color.holo_red_light, android.R.color.white)
                }
            })

            if (intentData?.getIntExtra("request_code", PROVINCE_REQUEST) == PROVINCE_REQUEST) {
                loadProvinceList()
            } else {
                val provinceCode = intentData?.getIntExtra("province_code", 0)
                loadRegionList(provinceCode.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    companion object {
        private const val PROVINCE_REQUEST = 0
        private const val CITY_REQUEST = 1
    }
}
