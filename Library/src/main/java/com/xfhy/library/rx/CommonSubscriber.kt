package com.xfhy.library.rx

import com.google.gson.JsonSyntaxException
import com.xfhy.library.basekit.view.BaseView
import com.xfhy.library.data.bean.BaseResp
import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * @author xfhy
 * time create at 2018/1/27 20:00
 * description 公用的订阅者
 */
open class CommonSubscriber<T>(var view: BaseView?, private val mErrorMsg: String = "") :
        ResourceSubscriber<T>() {

    override fun onNext(t: T?) {
        //统一的外层  封装一下
        if (t is BaseResp<*>) {
            if (t.errorCode == -1001) {
                //登录失效，需要重新登录
            } else if (t.errorCode != 0) {
                //其他错误
                view?.showErrorMsg(t.errorMsg)
            }
        }
        view?.hideLoading()
    }

    override fun onError(e: Throwable) {
        //关闭加载对话框
        view?.hideLoading()
        e.printStackTrace()
        when {
            mErrorMsg.isNotEmpty() -> {
                view?.showErrorMsg(mErrorMsg)
            }
            e is HttpException -> {
                if (e.code() == 504) {
                    view?.showErrorMsg("数据获取超时ヽ(≧Д≦)ノ")
                } else {
                    view?.showErrorMsg("数据加载失败ヽ(≧Д≦)ノ")
                }
            }
            e is SocketTimeoutException -> {
                view?.showErrorMsg("网络连接超时ヽ(≧Д≦)ノ")
            }
            e is JsonSyntaxException -> {
                view?.showErrorMsg("格式化数据出错ヽ(≧Д≦)ノ")
            }
            else -> {
                view?.showErrorMsg("未知错误ヽ(≧Д≦)ノ")
            }
        }
    }

    override fun onComplete() {
        //关闭加载对话框
        view?.hideLoading()
    }
}