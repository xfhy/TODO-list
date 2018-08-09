package com.xfhy.library.data.bean

/**
 * @author xfhy
 * time create at 2018/1/28 16:30
 * description 自己服务器 通用 返参 res
 */
class BaseResp<out T>(val code: Int, val msg: String, val data: T? = null)