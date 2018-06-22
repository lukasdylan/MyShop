package com.mobile.myshop.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ItemShippingCostResultBinding
import com.mobile.myshop.datamodel.response.CostResult

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/2/2018.
 */
class ShippingCostAdapter(private val costList: List<CostResult>, private val courierName: String) : RecyclerView.Adapter<ShippingCostAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemShippingCostResultBinding = DataBindingUtil.inflate<ItemShippingCostResultBinding>(LayoutInflater.from(parent.context),
                R.layout.item_shipping_cost_result, parent, false)
        return MainViewHolder(itemShippingCostResultBinding)
    }

    override fun getItemCount(): Int = costList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val provinceResult = costList[position]
        holder.bind(provinceResult, courierName)
    }

    inner class MainViewHolder(private val itemShippingCostResultBinding: ItemShippingCostResultBinding)
        : RecyclerView.ViewHolder(itemShippingCostResultBinding.root) {

        fun bind(costResult: CostResult, courierName: String) {
            itemShippingCostResultBinding.apply {
                image = when (courierName) {
                    "jne" -> R.drawable.logo_jne
                    "pos" -> R.drawable.pos_indonesia_logo
                    else -> R.drawable.tiki_logo
                }
                price = costResult.cost[0].value?.toDouble()
                serviceName = if (costResult.service != costResult.description) {
                    String.format("%s (%s)", costResult.service, costResult.description)
                } else {
                    costResult.service
                }
                costResult.cost[0].etd?.let {
                    estimationTime = when {
                        it.contains("HARI") -> {
                            String.format("%s hari", it.replace(Regex("""HARI"""), ""))
                        }
                        it.contains("JAM") -> {
                            String.format("%s jam", it.replace(Regex("""JAM"""), ""))
                        }
                        else -> String.format("%s hari", it)
                    }
                }
                executePendingBindings()
            }
        }
    }
}