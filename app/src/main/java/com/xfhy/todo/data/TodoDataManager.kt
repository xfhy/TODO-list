package com.xfhy.todo.data

import com.xfhy.library.data.net.RetrofitFactory
import com.xfhy.todo.BuildConfig
import com.xfhy.todo.data.api.LoginService
import com.xfhy.todo.data.api.TodoService
import com.xfhy.todo.data.bean.LoginBean
import com.xfhy.todo.data.bean.RegisterBean
import io.reactivex.Flowable

/**
 * Created by feiyang on 2018/8/10 12:12
 * Description :
 */
object TodoDataManager {
    private val mRetrofitFactory: RetrofitFactory by lazy { RetrofitFactory.instance }

    private val loginService by lazy { mRetrofitFactory.create(LoginService::class.java, BuildConfig.WAN_BASE_URL) }
    private val todoService by lazy { mRetrofitFactory.create(TodoService::class.java, BuildConfig.WAN_BASE_URL) }

    fun login(username: String, password: String): Flowable<LoginBean> {
        return loginService.login(username, password)
    }

    fun register(username: String, password: String): Flowable<RegisterBean> {
        return loginService.register(username, password, password)
    }

}