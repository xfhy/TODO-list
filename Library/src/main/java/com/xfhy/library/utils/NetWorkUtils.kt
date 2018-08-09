package com.xfhy.library.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by xfhy on 2018/2/4 17:03
 * Description :
 */
object NetWorkUtils {
    /**
     * 判断网络是否可用
     */
    @SuppressLint("MissingPermission")
    fun isNetWorkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * 检测wifi是否连接
     */
    @SuppressLint("MissingPermission")
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }
}