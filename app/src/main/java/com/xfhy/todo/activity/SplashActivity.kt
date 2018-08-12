package com.xfhy.todo.activity

import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xfhy.library.basekit.activity.BaseActivity
import com.xfhy.todo.R

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val rxPermissions = RxPermissions(this)

    }
}
