package com.xfhy.library.basekit.activity

import android.os.Bundle
import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.view.BaseView
import com.xfhy.library.widgets.LoadingDialog
import org.jetbrains.anko.toast

/**
 * @author xfhy
 * time create at 2018/1/27 9:09
 * description MVP Activity基类
 */
abstract class BaseMvpActivity<P : BasePresenter> : BaseActivity(), BaseView {
    protected var mPresenter: P? = null
    private val mDialog by lazy { LoadingDialog.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPresenter()
        mPresenter?.onCreate()
    }

    abstract fun initPresenter()

    /**
     * 显示加载中对话框
     */
    override fun showLoading() {
        mDialog.show()
    }

    /**
     * 隐藏加载中对话框
     */
    override fun hideLoading() {
        mDialog.hide()
    }

    override fun showEmptyView() {
    }

    override fun showOffline() {
    }

    override fun showContent() {
    }

    override fun showErrorMsg(errorMsg: String) {
        toast(errorMsg)
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
        mPresenter?.onDestroy()
    }

}