package com.xfhy.todo.data

import com.xfhy.library.data.bean.BaseResp
import com.xfhy.library.data.net.RetrofitFactory
import com.xfhy.library.utils.DateUtils
import com.xfhy.todo.BuildConfig
import com.xfhy.todo.common.Constant
import com.xfhy.todo.data.api.LoginService
import com.xfhy.todo.data.api.TodoService
import com.xfhy.todo.data.bean.LoginBean
import com.xfhy.todo.data.bean.RegisterBean
import com.xfhy.todo.data.bean.TodoBean
import io.reactivex.Flowable

/**
 * Created by feiyang on 2018/8/10 12:12
 * Description : 数据获取
 */
object TodoDataManager {
    private val mRetrofitFactory: RetrofitFactory by lazy { RetrofitFactory.instance }

    private val loginService by lazy { mRetrofitFactory.create(LoginService::class.java, BuildConfig.WAN_BASE_URL) }
    private val todoService by lazy { mRetrofitFactory.create(TodoService::class.java, BuildConfig.WAN_BASE_URL) }

    /**
     * 登录
     */
    fun login(username: String, password: String): Flowable<LoginBean> {
        return loginService.login(username, password)
    }

    /**
     * 注册
     */
    fun register(username: String, password: String): Flowable<RegisterBean> {
        return loginService.register(username, password, password)
    }

    /**
     * 获取未完成列表
     * @param page 从1开始
     */
    fun getUndoneTodoList(type: Int = Constant.TODO_TYPE, page: Int): Flowable<TodoBean> {
        return todoService.getUndoneTodoList(type, page)
    }

    /**
     * 获取已完成列表
     * @param page 从1开始
     */
    fun getDoneTodoList(type: Int = Constant.TODO_TYPE, page: Int): Flowable<TodoBean> {
        return todoService.getDoneTodoList(type, page)
    }

    /**
     * 标记某个todo 已完成
     * @param status 0:完成  1:未完成
     */
    fun markTodoStatus(id: Int, status: Int): Flowable<BaseResp<Any>> {
        return todoService.markTodoStatus(id, status)
    }

    /**
     * 删除某个todo
     */
    fun deleteTodoById(id: Int): Flowable<BaseResp<Any>> {
        return todoService.deleteTodoById(id)
    }

    /**
     * 更新todo内容
     * @param date yyyy-MM-dd
     * @param status 0:完成  1:未完成
     */
    fun updateTodoById(id: Int, title: String, content: String,
                       date: String = DateUtils.getDateFormatText(System.currentTimeMillis(), "yyyy-MM-dd"),
                       status: Int = Constant.UNDONE_STATES, type: Int = Constant.TODO_TYPE): Flowable<BaseResp<Any>> {
        return todoService.updateTodoById(id, title, content, date, status, type)
    }

    /**
     * 添加todo
     * @param date yyyy-MM-dd
     */
    fun addTodoById(title: String, content: String,
                    date: String = DateUtils.getDateFormatText(System.currentTimeMillis(), "yyyy-MM-dd"),
                    type: Int = Constant.TODO_TYPE): Flowable<BaseResp<Any>> {
        return todoService.addTodoById(title, content, date, type)
    }

}