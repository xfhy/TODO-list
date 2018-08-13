package com.xfhy.todo.presenter.impl

import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.todo.presenter.TodoFragmentContract

/**
 * Created by feiyang on 2018年8月13日11:52:46
 * Description : todo列表
 */
class TodoFragmentPresenter(private val mView: TodoFragmentContract.View) : RxPresenter(), TodoFragmentContract.Presenter {
    override fun getTodoList() {
    }
}