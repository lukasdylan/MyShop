package com.mobile.myshop.ui.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ItemProductListBinding
import com.mobile.myshop.datamodel.constant.AppConstant
import com.mobile.myshop.datamodel.response.ProductList
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/9/2018.
 */
class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.MainViewHolder>() {

    private var productList = mutableListOf<ProductList>()
    private var selectedProduct = PublishSubject.create<ProductList>()

    fun addNewProducts(products: List<ProductList>) {
        val oldSize = itemCount
        productList.addAll(products)
        notifyItemRangeInserted(oldSize, itemCount)
    }

    fun removeProducts() {
        notifyItemRangeRemoved(0, itemCount)
        productList.clear()
    }

    fun getCallback(): PublishSubject<ProductList> = selectedProduct

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val displayMetrics = DisplayMetrics()
        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val itemProductListBinding = DataBindingUtil.inflate<ItemProductListBinding>(LayoutInflater.from(parent.context), R.layout.item_product_list, parent, false)
        return itemProductListBinding.let {
            it.ivMenuIcon.layoutParams.width = displayMetrics.widthPixels * 10 / 35
            it.ivMenuIcon.layoutParams.height = displayMetrics.widthPixels * 10 / 35
            MainViewHolder(it)
        }
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    inner class MainViewHolder(private val itemProductListBinding: ItemProductListBinding) : RecyclerView.ViewHolder(itemProductListBinding.root) {
        fun bind(productList: ProductList) {
            itemProductListBinding.apply {
                setProductList(productList)
                price = productList.productPrice?.toDouble()
                productList.productPhotos?.let {
                    photoUrl = AppConstant.API_IMAGE_BASE_URL + it[0].photoName
                }
                rootLayout.setOnClickListener {
                    selectedProduct.onNext(productList)
                }
            }
        }
    }

}