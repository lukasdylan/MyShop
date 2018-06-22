package com.mobile.myshop.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ItemProvinceBinding
import com.mobile.myshop.datamodel.response.ProvinceResult
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/2/2018.
 */
class ProvinceAdapter(private val provinceList: List<ProvinceResult>) : RecyclerView.Adapter<ProvinceAdapter.MainViewHolder>() {

    private val selectedProvince: PublishSubject<ProvinceResult> = PublishSubject.create()

    fun getSelectedProvince(): PublishSubject<ProvinceResult> = selectedProvince

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemProvinceBinding = DataBindingUtil.inflate<ItemProvinceBinding>(LayoutInflater.from(parent.context),
                R.layout.item_province, parent, false)
        return MainViewHolder(itemProvinceBinding)
    }

    override fun getItemCount(): Int = provinceList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val provinceResult = provinceList[position]
        holder.bind(provinceResult)
    }

    inner class MainViewHolder(private val itemProvinceBinding: ItemProvinceBinding) : RecyclerView.ViewHolder(itemProvinceBinding.root) {

        fun bind(provinceResult: ProvinceResult) {
            itemProvinceBinding.apply {
                provinceName = "Provinsi " + provinceResult.province
                provinceNameTv.setOnClickListener {
                    selectedProvince.onNext(provinceResult)
                }
                executePendingBindings()
            }
        }
    }
}