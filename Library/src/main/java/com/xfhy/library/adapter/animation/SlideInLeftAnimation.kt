package com.xfhy.library.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


/**
 * 从左往右
 */
class SlideInLeftAnimation : BaseAnimation {
    override fun getAnimators(view: View?): Array<out Animator> {
        //view 的translationX 从 -顶层root view的宽度~0
        return arrayOf(ObjectAnimator.ofFloat(view, "translationX", -(view?.rootView?.width?.toFloat() ?:
                0f), 0f))
    }
}
