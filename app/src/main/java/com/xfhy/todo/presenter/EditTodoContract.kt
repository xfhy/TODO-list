package com.xfhy.todo.presenter

import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.view.BaseView
import com.xfhy.todo.data.bean.TodoBean

/**
 * Created by feiyang on 2018/8/10 10:38
 * Description : 编辑todo
 */
interface EditTodoContract {

    interface Presenter : BasePresenter {
        /**
         * 添加
         */
        fun addTodo(title: String, content: String, date: String)

        /**
         * 更新
         */
        fun update(id: Int, title: String, content: String, date: String, status: Int)
    }

    interface View : BaseView {
        /**
         * 添加成功
         */
        fun addSuccess(data: TodoBean.Data.TodoItem?)

        /**
         * 更新成功
         */
        fun updateSuccess()
    }

}