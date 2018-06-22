package com.mobile.myshop.ui.widget

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id>
 * on 6/10/2018.
 */
class FadePageTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        if (position <= -1.0F || position >= 1.0F) {
            page.translationX = page.width * position
            page.alpha = 0.0F
        } else if (position == 0.0F) {
            page.translationX = page.width * position
            page.alpha = 1.0F
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            page.translationX = page.width * -position
            page.alpha = 1.0F - Math.abs(position)
        }
    }
}