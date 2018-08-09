package com.xfhy.library.basekit.activity

import android.content.Context
import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.xfhy.library.common.AppManager

/**
 * @author xfhy
 * time create at 2018/1/27 9:09
 * description Activity基类
 */
abstract class BaseActivity : RxAppCompatActivity() {
    val mContext: Context by lazy { this }   //lazy只能用于val

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppManager.instance.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.finishActivity(this)
    }

}