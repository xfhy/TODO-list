package com.xfhy.library.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


/**
 * 从下往上
 */
class SlideInBottomAnimation : BaseAnimation {
    override fun getAnimators(view: View?): Array<out Animator> {
        //这里设置的是view的纵坐标,从view.getMeasuredHeight()-0,相当于从下往上移动自身的高度
        return arrayOf(ObjectAnimator.ofFloat(view, "translationY", (view?.measuredHeight?.toFloat() ?: 0f),
                0f))
    }
}
