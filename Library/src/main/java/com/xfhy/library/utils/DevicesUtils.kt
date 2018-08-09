package com.xfhy.library.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

import com.xfhy.library.common.BaseApplication

/**
 * Created by xfhy on 2017/9/24 21:57.
 * Description: 设备的工具类
 * 用来存放诸如:设备屏幕宽度  设置连接网络状态...
 */

object DevicesUtils {

    /**
     * 获取屏幕的大小(宽高)
     *
     * @return DisplayMetrics实例
     *
     *
     * displayMetrics.heightPixels是高度(单位是像素)
     * displayMetrics.heightPixels是宽度(单位是像素)
     */
    val devicesSize: DisplayMetrics
        get() {
            val displayMetrics = DisplayMetrics()
            val windowManager = BaseApplication.context.applicationContext
                    .getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            if (windowManager != null) {
                val defaultDisplay = windowManager.defaultDisplay
                defaultDisplay.getMetrics(displayMetrics)
                return displayMetrics
            }
            return DisplayMetrics()
        }

    /**
     * 跳转到设置界面
     *
     * @param context Context
     */
    @JvmStatic
    fun goSetting(context: Context?) {
        context?.let {
            val intent = Intent(Settings.ACTION_SETTINGS)
            it.startActivity(intent)
        }
    }

}
