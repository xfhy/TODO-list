package com.xfhy.library.basekit.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import com.xfhy.library.common.BroadcastConstant


/**
 * @author xfhy
 * time create at 2018/1/27 9:09
 * description Fragment基类
 */
abstract class BaseFragment : RxFragment() {
    /**
     * 该fragment所对应的布局
     */
    protected var mRootView: View? = null
    /**
     * 视图是否已经初始化
     */
    protected var isInit = false
    /**
     * 数据是否已经加载
     */
    protected var isLoad = false
    /**
     * 是否已经执行完onCreate()
     * 子类在执行完OnCreate()时记得设置为true  如果需要用此标志
     */
    protected var isCreated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //需要返回页面布局   所有子类需要返回view
        mRootView = inflater.inflate(getLayoutResId(), container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isInit = true  //视图已加载
        isCanLoadData() //初始化的时候去加载数据
    }

    /**
     * 视图是否已经对用户可见,系统的方法
     *
     * @param isVisibleToUser fragment对用用户是否可见了
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //去看看是否可以加载数据了
        isCanLoadData()
    }

    /**
     * 是否可以加载数据
     */
    private fun isCanLoadData() {
        if (!isInit) {
            return
        }

        //如果用户可见并且之前没有加载过数据  则去加载数据
        if (userVisibleHint && !isLoad) {
            lazyLoad()
        } else {
            if (isLoad) {
                stopLoad()
            }
        }
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     * 在加载完数据之后需要把isLoad置true
     */
    protected open fun lazyLoad() {
        isLoad = true
    }

    /**
     * 设置布局的id
     *
     * @return 返回子类布局id
     */
    protected abstract fun getLayoutResId(): Int

    /**
     * 当视图已经对用户不可见并且加载过数据,如果需要在切换到其他页面时停止加载,则可以重写该方法
     */
    protected open fun stopLoad() {}

    /**
     * 发送需要变换标题的广播   这样注册了该action的地方都会收到(前提:此应用&&未取消注册)
     */
    fun sendTitleChangeBroadcast(title: String) {
        val intent = Intent()
        intent.action = BroadcastConstant.MAIN_BROADCAST
        intent.putExtra(BroadcastConstant.TITLE_CHANGE_TITLE, title)
        context?.let {
            LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
        }
    }

    /**
     * 模拟物理按键返回
     */
    open fun onBackPress(): Boolean {
        return false
    }

}