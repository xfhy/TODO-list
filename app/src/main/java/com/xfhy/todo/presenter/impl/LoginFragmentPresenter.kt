package com.xfhy.todo.presenter.impl

import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.todo.presenter.LoginFragmentContract

/**
 * Created by feiyang on 2018/8/10 10:45
 * Description :
 */
class LoginFragmentPresenter(var mView: LoginFragmentContract.View) : RxPresenter(), LoginFragmentContract.Presenter {
    override fun login(name: String, password: String) {

    }
}