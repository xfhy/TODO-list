package com.xfhy.library.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.app.ActivityManager


/**
 * Created by xfhy on 2018/3/22 15:06
 * Description : Intent 工具类
 */
object IntentUtils {

    /**
     * 跳转到APP设置界面
     *
     * @param context Context
     */
    @JvmStatic
    fun goAppSetting(context: Context?, isNewTask: Boolean) {
        context?.let {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            var packageName = ""
            //当前应用pid
            val pid = android.os.Process.myPid()
            //任务管理类
            val manager = it.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            //遍历所有应用
            val infos = manager.runningAppProcesses
            infos.filter {
                //得到当前应用
                it.pid == pid
            }.forEach {
                packageName = it.processName//返回包名
            }
            intent.data = Uri.parse("package:" + packageName)
            if (isNewTask) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            it.startActivity(intent)
        }

    }

}