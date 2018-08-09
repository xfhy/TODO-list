package com.xfhy.library.basekit.presenter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author xfhy
 * create at 2017/12/17 9:23
 * description：基于RxJava2的Presenter封装,控制订阅的生命周期
 */
open class RxPresenter : BasePresenter {

    /*
    * 如果有多个Disposable , RxJava中已经内置了一个容器CompositeDisposable,
    * 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中, 在退出的时候, 调用CompositeDisposable
    * .clear() 即可切断所有的水管.
    * */
    protected var mCompositeDisposable: CompositeDisposable? = null

    /**
     * 切断水管,使得下游收不到事件
     */
    protected fun unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable?.clear()
        }
    }

    /**
     * 添加到容器中,方便控制
     *
     * @param disposable 用于解除订阅
     */
    protected fun addSubscribe(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    override fun onCreate() {

    }

    override fun onResume() {

    }

    override fun onDestroy() {
        unSubscribe()
    }

}
