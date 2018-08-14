package com.xfhy.library.basekit.fragment

import android.content.Context
import android.os.Bundle
import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.library.basekit.view.BaseView
import com.xfhy.library.widgets.LoadingDialog
import org.jetbrains.anko.support.v4.toast

/**
 * @author xfhy
 * time create at 2018/1/27 9:09
 * description 需要使用MVP的fragment的基类
 */
abstract class BaseMvpFragment<T : BasePresenter> : BaseFragment(), BaseView {
    protected var mPresenter: T? = null
    private val mDialog by lazy {
        activity?.let { LoadingDialog.create(it) }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        initPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter?.onCreate()
    }

    override fun showLoading() {
        mDialog?.show()
    }

    override fun hideLoading() {
        if(mDialog?.isShowing != false){
            mDialog?.hide()
        }
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
        mPresenter?.onDestroy()
    }

    override fun showContent() {
    }

    override fun showEmptyView() {
    }

    override fun showOffline() {
    }

    /**
     * 初始化presenter
     */
    abstract fun initPresenter()

}