package com.xfhy.library.data.net

import android.content.Context
import android.util.Log

import java.util.concurrent.TimeUnit

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by xfhy on 2017/9/24 22:18.
 * Description 网络模块的Utils
 */

object OkHttpUtils {
    /**
     * 获取OkHttp客户端
     * 用于管理所有的请求，内部支持并发，所以我们不必每次请求都创建一个 OkHttpClient
     * 对象，这是非常耗费资源的
     */
    @JvmStatic
    @get:Synchronized
    lateinit var okHttpClient: OkHttpClient
        private set

    /**
     * 最大缓存 10M
     */
    private val MAX_CACHE_SIZE = 10 * 1024 * 1024
    /**
     * 读取超时 20s
     */
    private val READ_TIME_OUT = 20
    /**
     * 连接超时
     */
    private val CONNECT_TIME_OUT = 15

    private val interceptor: Interceptor

    init {
        interceptor = Interceptor { chain ->
            val request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("charset", "utf-8")
                    .build()
            chain.proceed(request)
        }
    }

    /**
     * 初始化OkHttp客户端
     */
    @JvmStatic
    fun initClient(okHttpClient: OkHttpClient) {
        OkHttpUtils.okHttpClient = okHttpClient
    }

    /**
     * 初始化OkHttp
     */
    @JvmStatic
    fun initOkHttp(context: Context) {
        //拦截器
        val mRewriteCacheControlInterceptor = RewriteCacheControlInterceptor(context)

        //缓存文件
        val cacheFile = context.cacheDir
        //设置缓存大小
        val cache = Cache(cacheFile, MAX_CACHE_SIZE.toLong())
        if (cacheFile != null) {
            okHttpClient = OkHttpClient.Builder()
                    //超时设置
                    .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    //错误重连
                    .retryOnConnectionFailure(true)
                    //拦截器
                    .addNetworkInterceptor(mRewriteCacheControlInterceptor)   //缓冲拦截器
                    .addInterceptor(mRewriteCacheControlInterceptor)
                    .addInterceptor(interceptor)        //json
                    .addInterceptor(initLogInterceptor())  //日志拦截器
                    //缓存
                    .cache(cache)
                    .build()
        } else {
            okHttpClient = OkHttpClient.Builder()//超时设置
                    .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    //错误重连
                    .retryOnConnectionFailure(true)
                    //拦截器
                    .addNetworkInterceptor(mRewriteCacheControlInterceptor)   //缓冲拦截器
                    .addInterceptor(mRewriteCacheControlInterceptor)
                    .addInterceptor(interceptor)        //json
                    .addInterceptor(initLogInterceptor())  //日志拦截器
                    .build()
        }

    }

    /**
     * 日志拦截器
     */
    @JvmStatic
    private fun initLogInterceptor(): Interceptor {
        //实现HttpLoggingInterceptor中的一个输出日志的logger  方便打印w级别的日志
        val logger = HttpLoggingInterceptor.Logger { message ->
            Log.w("http_log", message)
        }

        val interceptor = HttpLoggingInterceptor(logger)
        //body
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

}
