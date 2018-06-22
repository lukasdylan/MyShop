package com.mobile.myshop.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ItemRegionBinding
import com.mobile.myshop.datamodel.response.CityResult
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/2/2018.
 */
class RegionAdapter(private val cityList: List<CityResult>) : RecyclerView.Adapter<RegionAdapter.MainViewHolder>() {

    private val selectedRegion: PublishSubject<CityResult> = PublishSubject.create()

    fun getSelectedRegion(): PublishSubject<CityResult> = selectedRegion

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemRegionBinding = DataBindingUtil.inflate<ItemRegionBinding>(LayoutInflater.from(parent.context),
                R.layout.item_region, parent, false)
        return MainViewHolder(itemRegionBinding)
    }

    override fun getItemCount(): Int = cityList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cityResult = cityList[position]
        holder.bind(cityResult)
    }

    inner class MainViewHolder(private val itemRegionBinding: ItemRegionBinding) : RecyclerView.ViewHolder(itemRegionBinding.root) {

        fun bind(cityResult: CityResult) {
            itemRegionBinding.apply {
                kodePos = "Kode Pos : "
                regionResult = cityResult
                mainLayout.setOnClickListener {
                    selectedRegion.onNext(cityResult)
                }
                executePendingBindings()
            }
        }
    }
}