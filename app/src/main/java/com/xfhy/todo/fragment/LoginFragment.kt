package com.xfhy.todo.fragment

import android.os.Bundle
import android.view.View
import com.xfhy.library.basekit.fragment.BaseMvpFragment
import com.xfhy.library.ext.enable
import com.xfhy.todo.R
import com.xfhy.todo.presenter.LoginFragmentContract
import com.xfhy.todo.presenter.impl.LoginFragmentPresenter
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by feiyang on 2018/8/10 10:36
 * Description :
 */
class LoginFragment : BaseMvpFragment<LoginFragmentPresenter>(), LoginFragmentContract.View, View.OnClickListener, View.OnFocusChangeListener {

    companion object {
        fun newInstance(): LoginFragment {
            val bundle = Bundle()

            val fragment = LoginFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

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
                mPresenter?.login(mUserNameEt.text.toString(), mPwdEt.text.toString())
            }
            R.id.mNoAccountTv -> {
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

    }

    override fun showErrorMsg(errorMsg: String) {

    }

}