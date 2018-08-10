package com.xfhy.todo.service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.xfhy.library.BuildConfig
import com.xfhy.library.data.net.OkHttpUtils

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class InitService : IntentService("InitService") {

    override fun onHandleIntent(intent: Intent?) {
        OkHttpUtils.initOkHttp(applicationContext)

        //日志打印
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    companion object {
        @JvmStatic
        fun startActionInit(context: Context) {
            val intent = Intent(context, InitService::class.java)
            context.startService(intent)
        }
    }
}
