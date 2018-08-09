package com.xfhy.library.widgets

import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.GridLayoutManager


/**
 * Created by xfhy on 2018/3/20 10:37
 * Description : 实现自己的ItemTouchHelper.Callback
 */
class DefaultItemTouchHelpCallback(private val onItemTouchCallbackListener: OnItemTouchCallbackListener) : ItemTouchHelper.Callback() {

    /**
     * 是否可以拖拽
     */
    var isCanDrag = false

    /**
     * 是否可以被滑动
     */
    var isCanSwipe = false

    /**
     * 当用户拖拽或者滑动Item的时候需要我们告诉系统滑动或者拖拽的方向
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {// GridLayoutManager
            // flag如果值是0，相当于这个功能被关闭
            val dragFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlag = 0
            // create make
            return makeMovementFlags(dragFlag, swipeFlag)
        } else if (layoutManager is LinearLayoutManager) {// linearLayoutManager
            val orientation = layoutManager.orientation

            var dragFlag = 0
            var swipeFlag = 0

            // 方向
            // or 表示 |
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (isCanSwipe) {
                    swipeFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                }
                dragFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else if (orientation == LinearLayoutManager.VERTICAL) {
                dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                if (isCanSwipe) {
                    swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                }
            }
            return makeMovementFlags(dragFlag, swipeFlag)
        }
        return 0
    }

    /**
     * 当Item被拖拽的时候被回调
     *
     * @param recyclerView     recyclerView
     * @param srcViewHolder    拖拽的ViewHolder
     * @param targetViewHolder 目的地的viewHolder
     * @return
     */
    override fun onMove(recyclerView: RecyclerView, srcViewHolder: ViewHolder, targetViewHolder: ViewHolder): Boolean {
        return onItemTouchCallbackListener.onMove(srcViewHolder.adapterPosition, targetViewHolder
                .adapterPosition)
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        onItemTouchCallbackListener.onSwiped(viewHolder.adapterPosition)
    }

    interface OnItemTouchCallbackListener {
        /**
         * 当某个Item被滑动删除的时候
         *
         * @param adapterPosition item的position
         */
        fun onSwiped(adapterPosition: Int)

        /**
         * 当两个Item位置互换的时候
         *
         * @param srcPosition    拖拽的item的position
         * @param targetPosition 目的地的Item的position
         * @return 处理了操作应该返回true，没有处理就返回false
         */
        fun onMove(srcPosition: Int, targetPosition: Int): Boolean
    }
}