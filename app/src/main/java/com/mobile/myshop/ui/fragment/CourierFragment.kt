package com.mobile.myshop.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.myshop.R
import com.mobile.myshop.databinding.LayoutCourierBottomSheetBinding
import com.mobile.myshop.ui.widget.RoundedBottomSheetFragment
import io.reactivex.subjects.PublishSubject

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/2/2018.
 */
class CourierFragment : RoundedBottomSheetFragment() {

    private val courierSubject: PublishSubject<String> = PublishSubject.create<String>()

    fun getListener(): PublishSubject<String> = courierSubject

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val courierBottomSheetBinding = DataBindingUtil.inflate<LayoutCourierBottomSheetBinding>(inflater, R.layout.layout_courier_bottom_sheet, container, false)
        return courierBottomSheetBinding.let {
            it.jneLogo = R.drawable.logo_jne
            it.posLogo = R.drawable.pos_indonesia_logo
            it.tikiLogo = R.drawable.tiki_logo
            it.jneLayout.setOnClickListener { courierSubject.onNext("JNE") }
            it.posIndonesiaLayout.setOnClickListener { courierSubject.onNext("Pos Indonesia") }
            it.tikiLayout.setOnClickListener { courierSubject.onNext("TIKI") }
            it.closeBtn.setOnClickListener { dismiss() }
            it.root
        }
    }
}