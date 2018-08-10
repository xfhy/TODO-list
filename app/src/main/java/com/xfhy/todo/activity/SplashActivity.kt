package com.xfhy.todo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xfhy.library.basekit.activity.BaseActivity
import com.xfhy.todo.R

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}
