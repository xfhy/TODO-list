package com.xfhy.todo.presenter

import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.view.BaseView

/**
 * Created by feiyang on 2018/8/10 10:38
 * Description : 编辑todo
 */
interface EditTodoContract {

    interface Presenter : BasePresenter {
        fun addTodo(title: String, content: String, date: String)

        fun update(id: Int, title: String, content: String, date: String)
    }

    interface View : BaseView {
        fun addSuccess()
        fun updateSuccess()
    }

}