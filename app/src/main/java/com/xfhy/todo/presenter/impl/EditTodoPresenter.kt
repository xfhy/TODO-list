package com.xfhy.todo.presenter.impl

import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.todo.presenter.EditTodoContract

/**
 * Created by feiyang on 2018/8/14 17:40
 * Description :
 */
class EditTodoPresenter(val mView: EditTodoContract.View) : RxPresenter(), EditTodoContract.Presenter {
    override fun addTodo(title: String, content: String, date: String) {

    }

    override fun update(id: Int, title: String, content: String, date: String) {

    }
}