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
        fun getTodoList()
    }

    interface View : BaseView {
        fun showTodoList(todoList: TodoBean)
    }

}