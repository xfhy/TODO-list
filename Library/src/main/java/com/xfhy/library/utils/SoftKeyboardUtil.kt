package com.xfhy.library.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by xfhy on 2018/3/5 9:50
 * Description : 软键盘工具类
 */
object SoftKeyboardUtil {
    /**
     * 隐藏软键盘
     *
     * @param context Context
     * @param view    输入text的view
     */
    @JvmStatic
    fun hideSoftInput(context: Context?, view: View?) {
        if (context == null || view == null) {
            return
        }
        val inputMethodManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager != null && inputMethodManager.isActive) {
            //隐藏虚拟键盘
            inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

    /**
     * 显示软键盘
     *
     * @param context Context
     * @param view    输入text的view
     */
    @JvmStatic
    fun showSoftKeyboard(context: Context?, view: View?) {
        if (context != null && view != null) {
            val inputMethodManager = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.showSoftInput(view, 0)
        }
    }
}