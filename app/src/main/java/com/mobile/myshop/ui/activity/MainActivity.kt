package com.mobile.myshop.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.mobile.myshop.R
import com.mobile.myshop.ui.adapter.HomeViewPagerAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseDaggerActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HomeViewPagerAdapter(supportFragmentManager).also {
            home_view_pager?.apply {
                offscreenPageLimit = it.count
                adapter = it
            }
        }

        bottom_navigation_view?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    home_view_pager.currentItem = 0
                    true
                }
                R.id.navigation_dashboard -> {
                    home_view_pager.currentItem = 1
                    true
                }
                R.id.navigation_others -> {
                    home_view_pager.currentItem = 2
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (home_view_pager.currentItem != 0) {
            bottom_navigation_view.selectedItemId = R.id.navigation_home
        } else {
            finish()
        }
    }

}
