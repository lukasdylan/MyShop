package com.mobile.myshop.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mobile.myshop.ui.fragment.HomeFragment
import com.mobile.myshop.ui.fragment.OrdersFragment

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/10/2018.
 */
class HomeViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private var fragmentList: MutableList<Fragment> = arrayListOf()

    init {
        fragmentList.apply {
            add(HomeFragment())
            add(OrdersFragment())
            add(OrdersFragment())
        }
    }

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

}