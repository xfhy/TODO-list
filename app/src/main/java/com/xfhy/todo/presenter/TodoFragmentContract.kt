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
         * 获取已完成列表
         */
        fun getDoneTodoList()

        /**
         * 标记某个todo 已完成
         * @param status 0:完成  1:未完成
         */
        fun markTodoStatus(id: Int, status: Int)

        /**
         * 删除某个todo
         */
        fun deleteTodoById(id: Int)

    }

    interface View : BaseView {
        fun showTodoList(todoData: TodoBean.Data)
    }

}