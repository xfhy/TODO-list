package com.xfhy.library.basekit.presenter

/**
 * author xfhy
 * create at 2017/9/15 13:44
 * description：MVP Presenter的父接口
 */
interface BasePresenter {

    /**
     * 模拟界面的生命周期 onCreate()
     */
    fun onCreate()

    /**
     * 模拟界面的生命周期 onResume()
     */
    fun onResume()

    /**
     * 模拟界面的生命周期 onDestroy()
     */
    fun onDestroy()

}
