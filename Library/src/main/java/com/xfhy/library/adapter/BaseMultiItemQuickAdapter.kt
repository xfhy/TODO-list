package com.xfhy.library.adapter

import android.support.annotation.LayoutRes
import android.util.SparseIntArray
import android.view.ViewGroup

import com.xfhy.library.adapter.entity.MultiItemEntity

/**
 * author xfhy
 * create at 2017/10/25 21:47
 * description：实现多布局需要继承自该类
 */
abstract class BaseMultiItemQuickAdapter<T : MultiItemEntity, K : BaseViewHolder>
(data: MutableList<T>) : BaseQuickAdapter<T, K>(data) {

    /**
     * layouts indexed with their types
     * key是type，value是layoutResId
     */
    private var layouts: SparseIntArray = SparseIntArray()

    override fun getDefItemViewType(position: Int): Int {
        val item = mData?.get(position)
        // 实体类必须实现MultiItemEntity接口  不然不知道item的类型
        return if (item is MultiItemEntity) {
            (item as MultiItemEntity).itemType
        } else DEFAULT_VIEW_TYPE
    }

    /**
     * 设置默认的type的布局
     */
    protected fun setDefaultViewTypeLayout(@LayoutRes layoutResId: Int) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
        return createBaseViewHolder(parent, getLayoutId(viewType))
    }

    private fun getLayoutId(viewType: Int): Int {
        return layouts.get(viewType, TYPE_NOT_FOUND)
    }

    /**
     * 增加item类型
     *
     * @param type        item类型
     * @param layoutResId item布局文件
     */
    protected fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        layouts.put(type, layoutResId)
    }

    companion object {

        private val DEFAULT_VIEW_TYPE = -0xff
        val TYPE_NOT_FOUND = -404
    }

}
