package com.xfhy.library.data.bean

import java.io.Serializable

/**
 * @author xfhy
 * time create at 2018/1/28 16:30
 * description 服务器 通用 返参 res
 */
open class BaseResp<T> : Serializable {
    var errorCode: Int = 0
    var errorMsg: String = ""
    var data: T? = null
}