package com.xfhy.todo

import com.xfhy.library.common.BaseApplication
import com.xfhy.todo.service.InitService

/**
 * Created by feiyang on 2018/8/10 10:10
 * Description :
 */
class TodoApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //初始化三方库
        InitService.startActionInit(this)
    }

}