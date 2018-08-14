package com.xfhy.todo.presenter

import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.view.BaseView
import com.xfhy.todo.data.bean.TodoBean

/**
 * Created by feiyang on 2018/8/10 10:38
 * Description :
 */
interface TodoFragmentContract {

    interface Presenter : BasePresenter {
        /**
         * 获取未完成列表
         */
        fun getUndoneTodoList()

        /**
         * 标记某个todo 已完成
         * @param status 0:完成  1:未完成
         */
        fun markTodoStatus(id: Int, status: Int)

        /**
         * 删除某个todo
         */
        fun deleteTodoById(id: Int)

        /**
         * 加载更多数据
         */
        fun loadMoreData(lastDateL: Long)

        /**
         * 刷新
         */
        fun onRefresh()

    }

    interface View : BaseView {
        /**
         * 展示todo 列表
         */
        fun showTodoList(todoData: MutableList<TodoBean.Data.TodoItem>)

        /**
         * 展示一个小提示消息
         */
        fun showTips(tips: String)

        /**
         * 刷新
         */
        fun onRefresh(todoData: MutableList<TodoBean.Data.TodoItem>)

        /**
         * 加载更多成功
         */
        fun loadMoreSuccess(todoData: MutableList<TodoBean.Data.TodoItem>)

        /**
         * 加载更多失败
         */
        fun loadMoreFailed()
    }

}