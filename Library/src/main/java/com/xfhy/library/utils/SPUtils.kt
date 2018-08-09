package com.xfhy.library.utils

import android.content.Context
import android.content.SharedPreferences
import com.xfhy.library.common.BaseApplication
import com.xfhy.library.common.BaseConstant

/**
 * Created by xfhy on 2018/2/6 21:44
 * Description :
 */
object SPUtils {

    private val sp: SharedPreferences  by lazy {
        BaseApplication.context.getSharedPreferences(BaseConstant.LIFE_HELPER_PREFS, Context.MODE_PRIVATE)
    }

    /**
     * 放置key和value到SharedPreferences中
     */
    fun putValue(key: String, value: Any) = with(sp.edit()) {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> {
                throw IllegalArgumentException("This type can be saved into Preferences")
            }
        }.apply()    //when语句是表达式,是带返回值的  相当于sp.edit().putXX().apply()    骚的一匹   减少了很多代码,相比java
    }

    /**
     * 获取指定key的SP值,带默认值
     */
    fun <T> getValue(key: String, default: T): T = with(sp) {
        //巧用with函数
        val res: Any = when (default) {
            is Boolean -> getBoolean(key, default)      //类型自动推断
            is String -> getString(key, default)
            is Int -> getInt(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)
            else -> {
                throw IllegalArgumentException("This type can not be saved into Preferences")
            }
        }
        res as T    //最后这句话是返回值
    }

    /**
     * 移除指定key
     */
    fun remove(key: String) {
        val edit = sp.edit()
        edit.remove(key)
        edit.apply()
    }

    /**
     * 删除全部数据
     */
    fun clear() {
        sp.edit().clear().apply()
    }

}