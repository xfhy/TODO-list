package com.xfhy.todo.data.api

import com.xfhy.todo.data.bean.LoginBean
import com.xfhy.todo.data.bean.RegisterBean
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by feiyang on 2018/8/10 12:15
 * Description : 登录模块相关api
 */
interface LoginService {

    /*
    * 5.1 登录
    http://www.wanandroid.com/user/login

    方法：POST
    参数：
        username，password
    5.2 注册
    http://www.wanandroid.com/user/register

    方法：POST
    参数
        username,password,repassword
    登录后会在cookie中返回账号密码，只要在客户端做cookie持久化存储即可自动登录验证。
    * */

    /*
    * @FormUrlEncoded将会自动将请求参数的类型调整为application/x-www-form-urlencoded
    * @Field注解将每一个请求参数都存放至请求体中
    * */

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Flowable<LoginBean>

    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") username: String, @Field("password") password: String
                 , @Field("repassword") rePassword: String): Flowable<RegisterBean>

}
/*
测试数据
* {
    "data": {
        "collectIds": [],
        "email": "",
        "icon": "",
        "id": 8816,
        "password": "xxxxxxx",
        "type": 0,
        "username": "xxxxxxx415456465465"
    },
    "errorCode": 0,
    "errorMsg": ""
}
* */