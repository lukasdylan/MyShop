package com.mobile.myshop.ui.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ItemDummyLoadingBinding
import com.mobile.myshop.databinding.ItemErrorHomeBinding
import com.mobile.myshop.databinding.ItemProductHomeBinding
import com.mobile.myshop.datamodel.constant.AppConstant
import com.mobile.myshop.datamodel.response.ProductList
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/24/2018.
 */

class HomeProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var productList: MutableList<ProductList> = arrayListOf()
    private var currentType = 1
    private val selectedProduct: PublishSubject<ProductList> = PublishSubject.create<ProductList>()
    private val refreshListener: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    fun changeLayoutType(type: Int, productList: MutableList<ProductList>) {
        currentType = type
        this@HomeProductAdapter.productList = productList
        notifyDataSetChanged()
    }

    fun getSelectedProduct(): PublishSubject<ProductList> = selectedProduct

    fun getRefresh(): PublishSubject<Boolean> = refreshListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val displayMetrics = DisplayMetrics()
        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        when (viewType) {
            LIST_TYPE -> {
                val itemProductHomeBinding = DataBindingUtil.inflate<ItemProductHomeBinding>(LayoutInflater.from(parent.context), R.layout.item_product_home, parent, false)
                itemProductHomeBinding.let {
                    it.rootLayout.layoutParams.width = displayMetrics.widthPixels / 3
                    it.rootLayout.layoutParams.height = displayMetrics.widthPixels / 2
                    return MainViewHolder(it)
                }
            }
            LOADING_TYPE -> {
                val itemDummyLoadingBinding = DataBindingUtil.inflate<ItemDummyLoadingBinding>(LayoutInflater.from(parent.context), R.layout.item_dummy_loading, parent, false)
                itemDummyLoadingBinding.let {
                    it.rootLayout.layoutParams.width = displayMetrics.widthPixels / 3
                    it.rootLayout.layoutParams.height = displayMetrics.widthPixels / 2
                    return LoadingViewHolder(it)
                }
            }
            else -> {
                val itemErrorHomeBinding = DataBindingUtil.inflate<ItemErrorHomeBinding>(LayoutInflater.from(parent.context), R.layout.item_error_home, parent, false)
                return ErrorViewHolder(itemErrorHomeBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = currentType

    override fun getItemCount(): Int {
        return when (currentType) {
            LIST_TYPE -> {
                if (productList.size >= 8) {
                    8
                } else {
                    productList.size
                }
            }
            LOADING_TYPE -> 5
            else -> 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainViewHolder -> {
                val productList = productList[position]
                holder.bind(productList)
            }
            is ErrorViewHolder -> {
                holder.itemView.setOnClickListener {
                    refreshListener.onNext(true)
                }
            }
        }
    }

    inner class MainViewHolder(private val itemProductHomeBinding: ItemProductHomeBinding) : RecyclerView.ViewHolder(itemProductHomeBinding.root) {

        fun bind(productList: ProductList) {
            itemProductHomeBinding.apply {
                setProductList(productList)
                price = productList.productPrice?.toDouble()
                productList.productPhotos?.let {
                    photoUrl = AppConstant.API_IMAGE_BASE_URL + it[0].photoName
                }
                rootLayout.setOnClickListener {
                    selectedProduct.onNext(productList)
                }
                executePendingBindings()
            }
        }
    }

    inner class LoadingViewHolder(itemDummyLoadingBinding: ItemDummyLoadingBinding) : RecyclerView.ViewHolder(itemDummyLoadingBinding.root)

    inner class ErrorViewHolder(itemErrorHomeBinding: ItemErrorHomeBinding) : RecyclerView.ViewHolder(itemErrorHomeBinding.root)

    companion object {
        const val LIST_TYPE = 0
        const val LOADING_TYPE = 1
        const val ERROR_TYPE = 2
    }
}