package com.xfhy.todo.broadcast

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.orhanobut.logger.Logger
import com.xfhy.library.common.AppManager
import com.xfhy.todo.activity.LoginActivity
import com.xfhy.todo.common.Constant

/**
 * Created by feiyang on 2018/10/11 9:23
 * Description : 接收掉线通知
 */
class OfflineReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Constant.OFFLINE_ACTION) {
            Logger.e("收到下线通知")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("提示")
                    .setCancelable(false)
                    .setMessage("登录失效，需要重新登录")
                    .setPositiveButton("确认") { _, _ ->
                        AppManager.instance.finishAllActivity()
                        val loginIntent = Intent(context, LoginActivity::class.java)
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context?.startActivity(loginIntent)
                    }
            builder.create().show()
        }
    }
}