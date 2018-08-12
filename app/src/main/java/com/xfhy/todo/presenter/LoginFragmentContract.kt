package com.xfhy.todo.presenter

import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.view.BaseView

/**
 * Created by feiyang on 2018/8/10 10:38
 * Description :
 */
interface LoginFragmentContract {

    interface Presenter : BasePresenter {
        fun login(name: String, password: String)
        fun register(name: String, password: String)
    }

    interface View : BaseView {
        fun loginSuccess()
    }

}