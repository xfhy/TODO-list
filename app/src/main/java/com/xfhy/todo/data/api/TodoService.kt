package com.xfhy.todo.data.api

import com.xfhy.library.data.bean.BaseResp
import com.xfhy.library.utils.DateUtils
import com.xfhy.todo.data.bean.TodoBean
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by feiyang on 2018/8/13 12:23
 * Description :
 */
interface TodoService {

    /**
     * 未完成 Todo 列表
    http://www.wanandroid.com/lg/todo/listnotdo/0/json/1

    http://www.wanandroid.com/lg/todo/listnotdo/类型/json/页码

    方法：POST
    参数：
    类型：类型拼接在链接上，目前支持0,1,2,3
    页码: 拼接在链接上，从1开始；
     * */
    @POST("lg/todo/listnotdo/{type}/json/{page}")
    fun getUndoneTodoList(@Path("type") type: Int, @Path("page") page: Int): Flowable<TodoBean>

    /**
    已完成 Todo 列表
    http://www.wanandroid.com/lg/todo/listdone/0/json/1

    http://www.wanandroid.com/lg/todo/listdone/类型/json/页码

    方法：POST
    参数：
    类型：类型拼接在链接上，目前支持0,1,2,3
    页码: 拼接在链接上，从1开始；
     * */
    @POST("lg/todo/listdone/{type}/json/{page}")
    fun getDoneTodoList(@Path("type") type: Int, @Path("page") page: Int): Flowable<TodoBean>

    /**
     * 仅更新完成状态Todo
    http://www.wanandroid.com/lg/todo/done/80/json

    方法：POST
    参数：
    id: 拼接在链接上，为唯一标识
    status: 0或1，传1代表未完成到已完成，反之则反之。
     * */
    @FormUrlEncoded
    @POST("lg/todo/done/{id}/json")
    fun markTodoStatus(@Path("id") id: Int, @Field("status") status: Int): Flowable<BaseResp<Any>>

    /**
     * 删除一条Todo
    http://www.wanandroid.com/lg/todo/delete/83/json

    方法：POST
    参数：
    id: 拼接在链接上，为唯一标识
     * */
    @POST("lg/todo/delete/{id}/json")
    fun deleteTodoById(@Path("id") id: Int): Flowable<BaseResp<Any>>

    /**
     * 更新一条Todo内容
    http://www.wanandroid.com/lg/todo/update/83/json

    方法：POST
    参数：
    id: 拼接在链接上，为唯一标识
    title: 更新标题
    content: 新增详情
    date: 2018-08-01
    status: 0 // 0为未完成，1为完成
    type: 0
     * */
    @FormUrlEncoded
    @POST("lg/todo/update/{id}/json")
    fun updateTodoById(@Path("id") id: Int, @Field("title") title: String, @Field("content") content: String,
                       @Field("date") date: String = DateUtils.getDateFormatText(System.currentTimeMillis(), "yyyy-MM-dd"),
                       @Field("status") status: Int = 0, @Field("type") type: Int = 0): Flowable<BaseResp<Any>>

    /**
     *  新增一条Todo
    http://www.wanandroid.com/lg/todo/add/json

    方法：POST
    参数：
    title: 新增标题
    content: 新增详情
    date: 2018-08-01
    type: 0
     */
    @FormUrlEncoded
    @POST("lg/todo/add/json")
    fun addTodoById(@Field("title") title: String, @Field("content") content: String,
                    @Field("date") date: String = DateUtils.getDateFormatText(System.currentTimeMillis(), "yyyy-MM-dd"),
                    @Field("type") type: Int = 0): Flowable<BaseResp<TodoBean.Data.TodoItem>>

}