package com.xfhy.library.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


/**
 * 渐显
 */
class AlphaInAnimation @JvmOverloads constructor(private val mFrom: Float = DEFAULT_ALPHA_FROM) : BaseAnimation {

    override fun getAnimators(view: View?): Array<out Animator> {
        //返回一个Animator数组    将动画附加到view上,透明度从mFrom-1f
        return arrayOf(ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f))
    }

    companion object {
        /**
         * 默认透明度从0开始
         */
        private val DEFAULT_ALPHA_FROM = 0f
    }
}
