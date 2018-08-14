package com.xfhy.todo.adapter

import android.view.View
import android.view.ViewGroup
import com.xfhy.library.adapter.BaseSectionQuickAdapter
import com.xfhy.library.adapter.BaseViewHolder
import com.xfhy.todo.R
import com.xfhy.todo.data.bean.TodoBean

/**
 * Created by feiyang on 2018/8/14 11:28
 * Description : 未完成列表adapter
 */
class TodoAdapter(itemData: MutableList<TodoBean.Data.TodoItem>?) : BaseSectionQuickAdapter<TodoBean.Data.TodoItem, BaseViewHolder>(R.layout
        .item_todo, R.layout.item_header_todo, itemData) {

    private var mListener: OnClickListener? = null

    override fun convert(holder: BaseViewHolder, item: TodoBean.Data.TodoItem?) {
        holder.setText(R.id.tv_item_title, item?.title)
        holder.setText(R.id.tv_item_content, item?.content)
        holder.setOnClickListener(R.id.iv_item_undo, View.OnClickListener { mListener?.doneTodoItem(holder.adapterPosition) })
        holder.setOnClickListener(R.id.iv_item_delete, View.OnClickListener { mListener?.deleteTodoItem(holder.adapterPosition) })
    }

    override fun convertHead(helper: BaseViewHolder, item: TodoBean.Data.TodoItem?) {
        helper.setText(R.id.tv_item_header_title, item?.header)
    }

    fun setOnClickListener(listener: OnClickListener) {
        mListener = listener
    }

    interface OnClickListener {
        /**
         * 完成
         */
        fun doneTodoItem(position: Int)

        /**
         * 删除
         */
        fun deleteTodoItem(position: Int)
    }

}