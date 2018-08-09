package com.xfhy.library.adapter.animation

import android.animation.Animator
import android.view.View

interface BaseAnimation {
    /**
     * 返回一个Animator数组,方便扩展,可以在view上加多个动画
     *
     * @param view
     * @return
     */
    fun getAnimators(view: View?): Array< out Animator>
}
