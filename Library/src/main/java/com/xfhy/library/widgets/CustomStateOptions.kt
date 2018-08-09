package com.xfhy.library.widgets

import android.support.annotation.DrawableRes
import android.view.View

import java.io.Serializable

/**
 * 自定义的状态,通过该配置model可以控制布局的显示
 */
class CustomStateOptions : Serializable {

    @DrawableRes
    var imageRes: Int = 0
        private set
    var isLoading: Boolean = false
        private set
    var message: String? = null
        private set
    var buttonText: String? = null
        private set
    var clickListener: View.OnClickListener? = null
        private set

    /**
     * 设置图片资源
     */
    fun image(@DrawableRes imageDrawable: Int): CustomStateOptions {
        imageRes = imageDrawable
        return this
    }

    fun loading(): CustomStateOptions {
        isLoading = true
        return this
    }

    /**
     * 设置需要显示的文字信息
     */
    fun message(msg: String): CustomStateOptions {
        message = msg
        return this
    }

    /**
     * 设置按钮文字
     */
    fun buttonText(msg: String): CustomStateOptions {
        buttonText = msg
        return this
    }

    /**
     * 设置按钮点击点听器
     */
    fun buttonClickListener(listener: View.OnClickListener): CustomStateOptions {
        clickListener = listener
        return this
    }

}
