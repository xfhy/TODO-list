package com.xfhy.todo.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.xfhy.library.basekit.fragment.BaseMvpFragment
import com.xfhy.library.ext.enable
import com.xfhy.library.utils.Base64Utils
import com.xfhy.library.utils.SPUtils
import com.xfhy.todo.R
import com.xfhy.todo.activity.MainActivity
import com.xfhy.todo.common.Constant
import com.xfhy.todo.presenter.LoginFragmentContract
import com.xfhy.todo.presenter.impl.LoginFragmentPresenter
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by feiyang on 2018/8/10 10:36
 * Description :
 */
class LoginFragment : BaseMvpFragment<LoginFragmentPresenter>(), LoginFragmentContract.View, View.OnClickListener, View.OnFocusChangeListener {

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }

        const val REGISTER = 1000
        const val LOGIN = 1001
    }

    private var mCurrentState = LOGIN

    override fun initPresenter() {
        mPresenter = LoginFragmentPresenter(this)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mLoginBtn.enable(mUserNameEt) { isLoginBtnEnable() }
        mLoginBtn.enable(mPwdEt) { isLoginBtnEnable() }

        mUserNameEt.setText(SPUtils.getValue(Constant.USERNAME, ""))
        val pwd = SPUtils.getValue(Constant.PASSWORD, "")
        if (!TextUtils.isEmpty(pwd)) {
            mPwdEt.setText(String(Base64Utils.decode(pwd)))
        }
        //mUserNameEt.setText("xxxxxxx415456465465")

        mLoginBtn.setOnClickListener(this)
        mNoAccountTv.setOnClickListener(this)
        mUserNameEt.onFocusChangeListener = this
        mPwdEt.onFocusChangeListener = this
    }

    /**
     * 当2个文本输入框都为非空时才让注册按钮设置为true
     */
    private fun isLoginBtnEnable(): Boolean {
        return mUserNameEt.text.isNullOrEmpty().not() &&
                mPwdEt.text.isNullOrEmpty().not()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mLoginBtn -> {
                if (mCurrentState == REGISTER) {
                    mPresenter?.register(mUserNameEt.text.toString(), mPwdEt.text.toString())
                } else if (mCurrentState == LOGIN) {
                    mPresenter?.login(mUserNameEt.text.toString(), mPwdEt.text.toString())
                }
            }
            R.id.mNoAccountTv -> {
                loginSuccess()
                /*if (mCurrentState == LOGIN) {
                    mCurrentState = REGISTER
                    mNoAccountTv.text = "去登录"
                    mConfirmPwdEt.visibility = View.VISIBLE
                    mLoginBtn.text = "注册并登录"
                } else {
                    mCurrentState = LOGIN
                    mNoAccountTv.text = "没有账号?去注册"
                    mConfirmPwdEt.visibility = View.GONE
                    mLoginBtn.text = "登录"
                }*/
            }
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            when (v?.id) {
                R.id.mUserNameEt -> {
                    mCurrentStateIv.setImageResource(R.mipmap.login_state_input)
                }
                R.id.mPwdEt -> {
                    mCurrentStateIv.setImageResource(R.mipmap.login_state_password)
                }
            }
        }
    }

    override fun loginSuccess() {
        startActivity<MainActivity>()
    }

}