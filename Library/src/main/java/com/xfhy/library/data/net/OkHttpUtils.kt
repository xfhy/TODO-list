package com.xfhy.library.data.net

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xfhy.library.utils.SPUtils
import okhttp3.*

import java.util.concurrent.TimeUnit

import okhttp3.logging.HttpLoggingInterceptor
import java.io.File


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
    var okHttpClient: OkHttpClient? = null

    /**
     * 最大缓存 10M
     */
    private const val MAX_CACHE_SIZE = 10 * 1024 * 1024
    /**
     * 读取超时 20s
     */
    private const val READ_TIME_OUT = 20
    /**
     * 连接超时
     */
    private const val CONNECT_TIME_OUT = 15

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

        //缓存文件夹
        val cacheFile = File(context.externalCacheDir.toString(), "cache")
        //设置缓存大小
        val cache = Cache(cacheFile, MAX_CACHE_SIZE.toLong())
        okHttpClient = OkHttpClient.Builder()
                //超时设置
                .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true)
                //拦截器
                //.addNetworkInterceptor(mRewriteCacheControlInterceptor)   //缓冲拦截器
                //.addInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(interceptor)        //json
                .addInterceptor(initLogInterceptor())  //日志拦截器
                //缓存
                .cache(cache)
                //cookie 自动管理
                .cookieJar(object : CookieJar {
                    private var cookieStore = java.util.HashMap<String, MutableList<Cookie>>()
                    private val gson = Gson()
                    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
                        url ?: return
                        cookies ?: return

                        //登录成功则不存cookie
                        if(!SPUtils.getValue("is_login",false)){
                            //HashMap 存值  将url的host存起来  domain是域名   host是主机
                            cookieStore[url.host()] = cookies

                            //将cookie序列化到SP中  避免下次进入APP时cookie没有了之前的cookie
                            SPUtils.putValue(url.host(), gson.toJson(cookieStore))
                        }
                    }

                    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
                        val cookieList = SPUtils.getValue(url?.host() ?: "", "")
                        cookieStore = gson.fromJson(cookieList, object : TypeToken<java.util.HashMap<String, MutableList<Cookie>>>() {}
                                .type) ?: java.util.HashMap()
                        //cookieStore = gson.fromJson(cookieList, cookieStore.javaClass)
                        return cookieStore[url?.host()] ?: mutableListOf()
                    }
                })
                .build()

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
