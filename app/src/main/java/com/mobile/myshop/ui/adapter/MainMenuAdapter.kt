package com.mobile.myshop.ui.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.ItemMainMenuBinding
import com.mobile.myshop.datamodel.entity.MainMenu
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 5/18/2018.
 */
class MainMenuAdapter(private val menuList: List<MainMenu>) : RecyclerView.Adapter<MainMenuAdapter.MenuViewHolder>() {

    private val publishSubject = PublishSubject.create<Int>()

    fun getCallback(): PublishSubject<Int> = publishSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemMainMenuBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_main_menu, parent, false) as ItemMainMenuBinding
        val displayMetrics = DisplayMetrics()
        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        itemMainMenuBinding.let {
            it.rootLayout.layoutParams.width = displayMetrics.widthPixels * 10 / 35
            it.rootLayout.layoutParams.height = displayMetrics.widthPixels * 10 / 35
            return MenuViewHolder(it)
        }
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val mainMenu = menuList[position]
        holder.apply {
            bind(mainMenu)
            itemView.setOnClickListener { publishSubject.onNext(position) }
        }
    }

    inner class MenuViewHolder(private val itemMainMenuBinding: ItemMainMenuBinding) : RecyclerView.ViewHolder(itemMainMenuBinding.root) {
        fun bind(mainMenu: MainMenu) {
            itemMainMenuBinding.apply {
                setMainMenu(mainMenu)
                val backgroundCircleDrawable = ivMenuIcon.background as GradientDrawable
                backgroundCircleDrawable.setColor(ContextCompat.getColor(root.context, mainMenu.menuBackgroundColor))
                executePendingBindings()
            }
        }
    }
}