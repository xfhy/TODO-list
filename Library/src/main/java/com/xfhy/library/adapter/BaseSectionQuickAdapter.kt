package com.xfhy.library.adapter

import android.view.ViewGroup

import com.xfhy.library.adapter.entity.SectionEntity

/**
 * author xfhy
 * create at 2017/10/24 13:40
 * description：分组布局时需要使用该adapter
 */
abstract class BaseSectionQuickAdapter<T : SectionEntity<T>, K : BaseViewHolder>
(layoutResId: Int,
 /**
  * 分组header布局id
  */
 protected var mSectionHeadResId: Int, data: MutableList<T>?) : BaseQuickAdapter<T, K>(layoutResId, data) {

    override fun getDefItemViewType(position: Int): Int {
        //item的类型 根据实体类里的一个属性  分组RecyclerView里,要么是分组header,要么是普通item
        return if ( (mData?.get(position)?.isHeader ?: false) ) SECTION_HEADER_VIEW else 0
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
        //创建ViewHolder  如果是分组header,那么view是mSectionHeadResId加载出来的
        return if (viewType == SECTION_HEADER_VIEW) createBaseViewHolder(getItemView(mSectionHeadResId, parent)) else super.onCreateDefViewHolder(parent, viewType)
        //分组内的item,则用默认的方法创建ViewHolder
    }

    override fun isFixedViewType(type: Int): Boolean {
        //分组header也是特殊布局,也需要进行跨格子(在GridLayoutManager中,比如SpanCount为2,那么分组header的跨度就是2)
        return super.isFixedViewType(type) || type == SECTION_HEADER_VIEW
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        when (holder.itemViewType) {
            SECTION_HEADER_VIEW -> {
                //如果是分组header,那么需要设置为满Span  即占满
                setFullSpan(holder)
                //绑定数据  这是分组header
                convertHead(holder, getItem(position - headerLayoutCount))
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }

    /**
     * 分组header绑定数据
     *
     * @param helper ViewHolder
     * @param item   实体类
     */
    protected abstract fun convertHead(helper: K, item: T?)

    companion object {
        /**
         * 分组header类型
         */
        protected const val SECTION_HEADER_VIEW = 0x00000444
    }
}
