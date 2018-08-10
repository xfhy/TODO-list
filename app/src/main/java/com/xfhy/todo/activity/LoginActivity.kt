package com.xfhy.todo.activity

import android.os.Bundle
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.todo.R
import com.xfhy.todo.fragment.LoginFragment

private const val LOGIN_TAG = "login_tag"

class LoginActivity : TitleBarActivity() {

    private val mLoginFragment by lazy { LoginFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
    }

    private fun initView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_login_root_view, LoginFragment.newInstance(), LOGIN_TAG)
        transaction.commit()
    }

    override fun getThisTitle(): CharSequence = "登录"
}
