package com.xfhy.library.adapter.entity

import java.io.Serializable

/**
 * author xfhy
 * create at 2017/10/24 13:35
 * description：所有分组布局实体类的父类
 */
open class SectionEntity<T> : Serializable {
    /**
     * 是否是分组header
     */
    var isHeader: Boolean = false
    /**
     * 包装的实体类,可以没有
     */
    var t: T? = null
    /**
     * 分组header标题
     */
    var header: String? = null

    constructor() {}

    /**
     * @param isHeader true:是分组header false:不是
     */
    constructor(isHeader: Boolean) {
        this.isHeader = isHeader
        this.header = null
        this.t = null
    }

    constructor(t: T) {
        this.isHeader = false
        this.header = null
        this.t = t
    }

    constructor(isHeader: Boolean, header: String) {
        this.isHeader = isHeader
        this.header = header
        this.t = null
    }


}
