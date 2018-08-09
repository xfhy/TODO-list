package com.xfhy.library.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


/**
 * 缩放
 */
class ScaleInAnimation @JvmOverloads constructor(private val mFrom: Float = DEFAULT_SCALE_FROM) : BaseAnimation {

    override fun getAnimators(view: View?): Array<out Animator> {
        //设置scaleX和scaleY都从mFrom-1f
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)
        return arrayOf<ObjectAnimator>(scaleX, scaleY)
    }

    companion object {
        private val DEFAULT_SCALE_FROM = .5f
    }
}
