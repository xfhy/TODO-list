package com.xfhy.todo.service

import android.app.IntentService
import android.app.Notification
import android.content.Intent
import android.content.Context
import android.os.Build
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.xfhy.library.BuildConfig
import com.xfhy.library.data.net.OkHttpUtils
import android.app.NotificationManager
import android.app.NotificationChannel
import android.annotation.TargetApi

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class InitService : IntentService("InitService") {

    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //通知渠道
            val channel = NotificationChannel("start", "start",
                    NotificationManager.IMPORTANCE_HIGH)

            //创建渠道
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            val notification = Notification.Builder(this, "start").build()
            startForeground(1, notification)
        }
    }

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
            //8.0以上区别对待 开启后台Service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}
