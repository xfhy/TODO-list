package com.xfhy.library.basekit.presenter

import android.content.Context
import com.trello.rxlifecycle2.LifecycleProvider
import com.xfhy.library.basekit.view.BaseView
import com.xfhy.library.utils.NetWorkUtils

/**
 * @author xfhy
 * time create at 2018/1/27 9:07
 * description
 */
open class AbstractPresenter : BasePresenter{

    /**
     * 用于传入RxKotlin 避免内存泄露
     */
    lateinit var lifeProvider: LifecycleProvider<*>

    override fun onCreate() {
    }

    override fun onResume() {
    }

    override fun onDestroy() {
    }
}
