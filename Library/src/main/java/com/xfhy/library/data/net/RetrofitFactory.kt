package com.xfhy.library.data.net

import com.xfhy.library.common.BaseApplication
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author xfhy
 * time create at 2018/1/28 10:09
 * description Retrofit工厂
 */
class RetrofitFactory private constructor() {

    companion object {
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    /**
     * 具体服务实例化
     */
    fun <T> create(service: Class<T>, baseUrl: String): T {
        if (OkHttpUtils.okHttpClient == null) {
            OkHttpUtils.initOkHttp(BaseApplication.context)
        }
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpUtils.okHttpClient)
                .build()
        return retrofit.create(service)
    }

}