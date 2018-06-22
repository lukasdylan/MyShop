package com.mobile.myshop.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ActivityProductListBinding
import com.mobile.myshop.ui.adapter.ProductListAdapter
import com.mobile.myshop.utils.UiUtils
import com.mobile.myshop.viewmodel.ProductListViewModel
import com.mobile.myshop.viewmodel.ProductListViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class ProductListActivity : BaseDaggerActivity() {

    @Inject
    lateinit var productListViewModelFactory: ProductListViewModelFactory

    private var productListBinding: ActivityProductListBinding? = null
    private var productListAdapter: ProductListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productListBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_list) as ActivityProductListBinding
        productListBinding?.apply {
            viewModel = ViewModelProviders.of(this@ProductListActivity, productListViewModelFactory).get(ProductListViewModel::class.java)
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowHomeEnabled(true)
                setDisplayHomeAsUpEnabled(true)
            }
            productListRv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                productListAdapter = ProductListAdapter().apply {
                    getCallback().subscribe({
                        Timber.d(it.productName)
                    })
                }
                adapter = productListAdapter
            }

            viewModel?.let { initViewModel(it) }

        }
    }

    private fun initViewModel(productListViewModel: ProductListViewModel) {
        productListViewModel.apply {
            productListResult.observe(this@ProductListActivity, Observer {
                it?.let {
                    productListAdapter?.addNewProducts(it)
                }
            })

            errorMessage.observe(this@ProductListActivity, Observer {
                it?.let {
                    productListBinding?.run {
                        UiUtils.showSnackBar(coordinatorLayout, it, android.R.color.holo_red_light, android.R.color.white)
                    }
                }
            })

            fabEvent.observe(this@ProductListActivity, Observer {
                Timber.d("FAB Clicked")
            })

            loadProductList()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }
}
