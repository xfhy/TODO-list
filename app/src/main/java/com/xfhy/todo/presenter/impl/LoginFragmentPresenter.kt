package com.xfhy.todo.presenter.impl

import com.orhanobut.logger.Logger
import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.library.rx.CommonSubscriber
import com.xfhy.library.rx.scheduler.SchedulerUtils
import com.xfhy.library.utils.Base64Utils
import com.xfhy.library.utils.SPUtils
import com.xfhy.todo.common.Constant
import com.xfhy.todo.data.TodoDataManager
import com.xfhy.todo.data.bean.LoginBean
import com.xfhy.todo.presenter.LoginFragmentContract

/**
 * Created by feiyang on 2018/8/10 10:45
 * Description : 登录
 */
class LoginFragmentPresenter(private val mView: LoginFragmentContract.View) : RxPresenter(), LoginFragmentContract.Presenter {
    override fun login(name: String, password: String) {
        mView.showLoading()
        saveInputData(name, password)
        addSubscribe(TodoDataManager.login(name, password).compose(SchedulerUtils.ioToMain()).subscribeWith
        (object : CommonSubscriber<LoginBean>(mView) {
            override fun onNext(t: LoginBean?) {
                super.onNext(t)
                t.let {
                    Logger.e(it.toString())
                    mView.loginSuccess()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                mView.showErrorMsg("登录失败")
            }
        }))
    }

    /**
     * 将输入信息缓存
     */
    private fun saveInputData(name: String, password: String) {
        SPUtils.putValue(Constant.USERNAME, name)
        SPUtils.putValue(Constant.PASSWORD, Base64Utils.encode(password.toByteArray()))
    }
}