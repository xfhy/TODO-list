package com.xfhy.library.basekit.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.xfhy.library.R

import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.library.basekit.view.BaseView
import com.xfhy.library.common.BroadcastConstant
import com.xfhy.library.utils.DevicesUtils
import com.xfhy.library.utils.SnackbarUtil
import com.xfhy.library.widgets.StatefulLayout

/**
 * @author xfhy
 * create at 2018年3月10日16:45:18
 * description：所有需要刷新和请求网络的fragment可以继承自该fragment,已实现基本的展示空布局,刷新布局,显示错误信息等
 * MVP架构,布局中必须是有StatefulLayout,并且子类必须去fbc该id
 */
abstract class BaseStateMVPFragment<P : RxPresenter> : BaseFragment(), BaseView {

    protected var mPresenter: P? = null

    var mStateView: StatefulLayout? = null

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        initPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter?.onCreate()
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
    }

    /**
     * 初始化presenter
     */
    abstract fun initPresenter()

    override fun showErrorMsg(errorMsg: String) {
        hideLoading()
        mRootView?.let {
            SnackbarUtil.showBarLongTime(it, errorMsg, SnackbarUtil.ALERT)
        }
    }

    override fun showEmptyView() {
        closeRefresh()
        mStateView?.showEmpty(R.string.stfLayoutEmptyMessage, R.string.stfLayoutButtonRetry)
    }

    /**
     * 停止刷新
     */
    abstract fun closeRefresh()

    override fun showOffline() {
        closeRefresh()
        mStateView?.showOffline(R.string.stfLayoutOfflineMessage, R.string.stfLayoutButtonSetting, View.OnClickListener {
            //未联网  跳转到设置界面
            DevicesUtils.goSetting(activity)
        })
    }


    override fun showContent() {
        closeRefresh()
        mStateView?.showContent()
    }

    override fun showLoading() {
        mStateView?.showLoading()
    }

    override fun hideLoading() {
        mStateView?.showContent()
    }

    override fun onBackPress(): Boolean {
        return false
    }

}
