package com.mobile.myshop.ui.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.FragmentHomeBinding
import com.mobile.myshop.datamodel.entity.FavoriteProduct
import com.mobile.myshop.datamodel.response.ProductList
import com.mobile.myshop.ui.activity.ProductListActivity
import com.mobile.myshop.ui.activity.ShippingCostActivity
import com.mobile.myshop.ui.adapter.HomeFavoriteAdapter
import com.mobile.myshop.ui.adapter.HomeProductAdapter
import com.mobile.myshop.ui.adapter.MainMenuAdapter
import com.mobile.myshop.ui.widget.GridSpacingItemDecoration
import com.mobile.myshop.utils.UiUtils
import com.mobile.myshop.viewmodel.HomeViewModel
import com.mobile.myshop.viewmodel.HomeViewModelFactory
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private var homeViewModel: HomeViewModel? = null
    private var fragmentHomeBinding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this, homeViewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false) as FragmentHomeBinding
        return fragmentHomeBinding?.let {
            it.viewModel = homeViewModel
            it.rvNewestProduct.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(GridSpacingItemDecoration(8, resources.getDimension(R.dimen.space_x2).toInt(), true))
                isNestedScrollingEnabled = false
            }
            it.rvMenu.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 3)
                isNestedScrollingEnabled = false
            }
            it.rvFavoriteProduct.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(GridSpacingItemDecoration(5, resources.getDimension(R.dimen.space_x2).toInt(), true))
                isNestedScrollingEnabled = false
            }
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel?.apply {
            mainMenuList.observe(this@HomeFragment, Observer {
                it?.apply {
                    val mainMenuAdapter = MainMenuAdapter(this).apply {
                        getCallback().subscribe { position ->
                            when (position) {
                                0 -> {
                                    openActivity(ProductListActivity(), null, Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                                }
                                1 -> {
                                    openActivity(ShippingCostActivity(), null, Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                                }
                                2 -> {

                                }
                            }
                        }
                    }
                    fragmentHomeBinding?.rvMenu?.apply {
                        addItemDecoration(GridSpacingItemDecoration(size, resources.getDimension(R.dimen.space_x2).toInt(), true))
                        adapter = mainMenuAdapter
                    }
                }
            })

            latestProductResult.observe(this@HomeFragment, Observer {
                it?.apply {
                    fragmentHomeBinding?.rvNewestProduct?.adapter = HomeProductAdapter().apply {
                        changeLayoutType(first, second as MutableList<ProductList>)
                        getSelectedProduct().subscribe {
                            Timber.d(it.productName)
                        }
                        getRefresh().subscribe {
                            if (it) {
                                loadLatestProduct()
                            }
                        }
                    }
                }
            })

            favoriteProductResult.observe(this@HomeFragment, Observer {
                it?.apply {
                    fragmentHomeBinding?.rvFavoriteProduct?.adapter = HomeFavoriteAdapter().apply {
                        changeLayoutType(first, second as MutableList<FavoriteProduct>)
                        getCallback().subscribe {
                            Timber.d(it.productName)
                        }
                    }
                }
            })

            errorMessage.observe(this@HomeFragment, Observer {
                it?.apply {
                    fragmentHomeBinding?.coordinatorLayout?.let { it1 ->
                        UiUtils.showTopSnackBar(it1, this, android.R.color.holo_red_light, android.R.color.white)
                    }
                }
            })

            viewMoreNavigation.observe(this@HomeFragment, Observer {
                it?.apply {
                    when (it) {
                        HomeViewModel.VIEW_MORE_LATEST_PRODUCT -> {
                            openActivity(ProductListActivity(), null, Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                        }
                        HomeViewModel.VIEW_MORE_FAVORITE_PRODUCT -> {

                        }
                    }
                }
            })
            loadGreetings()
            loadCustomerName()
            loadMainMenu()
            loadLatestProduct()
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel?.loadFavoriteProducts()
    }
}
