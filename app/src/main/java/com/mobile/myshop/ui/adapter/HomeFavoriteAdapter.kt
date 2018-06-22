package com.mobile.myshop.ui.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ItemEmptyFavoriteBinding
import com.mobile.myshop.databinding.ItemFavoriteProductHomeBinding
import com.mobile.myshop.datamodel.constant.AppConstant
import com.mobile.myshop.datamodel.entity.FavoriteProduct
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/1/2018.
 */
class HomeFavoriteAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentType = 0
    private var favoriteProductList: MutableList<FavoriteProduct> = arrayListOf()
    private val selectedProduct: PublishSubject<FavoriteProduct> = PublishSubject.create<FavoriteProduct>()

    fun changeLayoutType(type: Int, productList: MutableList<FavoriteProduct>) {
        currentType = type
        if (productList.isNotEmpty()) {
            this@HomeFavoriteAdapter.favoriteProductList = productList
        }
        notifyDataSetChanged()
    }

    fun getCallback(): PublishSubject<FavoriteProduct> = selectedProduct

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val displayMetrics = DisplayMetrics()
        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        when (viewType) {
            LIST_TYPE -> {
                val itemProductHomeBinding = DataBindingUtil.inflate<ItemFavoriteProductHomeBinding>(LayoutInflater.from(parent.context), R.layout.item_favorite_product_home, parent, false)
                itemProductHomeBinding.let {
                    it.rootLayout.layoutParams.width = displayMetrics.widthPixels * 10 / 30
                    it.rootLayout.layoutParams.height = displayMetrics.widthPixels * 10 / 20
                    return MainViewHolder(it)
                }
            }
            else -> {
                val itemEmptyFavoriteBinding = DataBindingUtil.inflate<ItemEmptyFavoriteBinding>(LayoutInflater.from(parent.context), R.layout.item_empty_favorite, parent, false)
                return EmptyViewHolder(itemEmptyFavoriteBinding)
            }
        }
    }

    override fun getItemCount(): Int {
        return when (currentType) {
            LIST_TYPE -> {
                if (favoriteProductList.size >= 5) {
                    5
                } else {
                    favoriteProductList.size
                }
            }
            else -> 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainViewHolder -> {
                val favoriteList = favoriteProductList[position]
                holder.bind(favoriteList)
            }
        }
    }

    inner class MainViewHolder(private val itemProductHomeBinding: ItemFavoriteProductHomeBinding) : RecyclerView.ViewHolder(itemProductHomeBinding.root) {

        fun bind(productList: FavoriteProduct) {
            itemProductHomeBinding.apply {
                favoriteProduct = productList
                price = productList.productPrice.toDouble()
                productList.productPhotos.let {
                    photoUrl = AppConstant.API_IMAGE_BASE_URL + it[0]
                }
                rootLayout.setOnClickListener { selectedProduct.onNext(productList) }
                executePendingBindings()
            }
        }
    }

    inner class EmptyViewHolder(itemEmptyFavoriteBinding: ItemEmptyFavoriteBinding) : RecyclerView.ViewHolder(itemEmptyFavoriteBinding.root)

    companion object {
        const val LIST_TYPE = 1
        const val EMPTY_TYPE = 2
    }
}