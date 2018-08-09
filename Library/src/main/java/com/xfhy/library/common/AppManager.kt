package com.xfhy.library.common

import android.app.Activity
import android.content.Context
import java.util.*

/**
 * Created by xfhy on 2018/2/3 12:58
 * Description : 用于管理Activity栈
 */
class AppManager private constructor() {

    val activityStack = Stack<Activity>()

    companion object {
        val instance: AppManager by lazy { AppManager() }
    }

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun finishActivity(activity: Activity) {
        activity.finish()
        activityStack.remove(activity)
    }

    fun finishAllActivity() {
        activityStack.forEach {
            it.finish()
        }
        activityStack.clear()
    }

    fun exitApp(context: Context) {
        finishAllActivity()
        System.exit(0)
    }

}