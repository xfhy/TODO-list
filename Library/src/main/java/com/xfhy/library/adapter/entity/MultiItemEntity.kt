package com.xfhy.library.adapter.entity

/**
 * author xfhy
 * create at 2017/10/25 21:48
 * description：多布局的实体类必须实现此接口
 */
interface MultiItemEntity {
    /**
     * 获取该item的type
     */
    val itemType: Int
}
