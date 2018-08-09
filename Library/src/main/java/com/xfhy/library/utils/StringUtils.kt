package com.xfhy.library.utils

import java.util.regex.Pattern

/**
 * Created by xfhy on 2018/3/3 15:54
 * Description : 字符串工具类
 */
object StringUtils {

    /**
     * 验证邮箱
     */
    @JvmStatic
    fun isEmail(text: String) = Pattern.matches("[_a-zA-Z0-9]+@[0-9a-zA-Z]+(\\.[a-zA-Z]+)+", text)

    /**
     * 验证手机号码
     */
    @JvmStatic
    fun isPhone(text: String): Boolean {
        return Pattern.compile("^1\\d{10}$").matcher(text).matches()
    }

}