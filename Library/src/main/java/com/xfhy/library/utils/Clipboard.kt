package com.xfhy.library.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * @author xfhy
 * @create at 2017/11/18 22:09
 * description：剪贴板工具类
 */
object Clipboard {

    /**
     * 复制文本到系统剪贴板中
     */
    fun copyText(context: Context, text: String) {
        // as :强制转换
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //创建ClipData对象
        val clipData = ClipData.newPlainText("", text)
        //添加ClipData对象到剪切板中
        clipboard.primaryClip = clipData
    }

}