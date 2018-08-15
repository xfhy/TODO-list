package com.xfhy.todo.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.xfhy.library.basekit.fragment.BaseFragment
import com.xfhy.library.common.AppManager
import com.xfhy.library.utils.SPUtils
import com.xfhy.todo.R
import com.xfhy.todo.activity.LoginActivity
import com.xfhy.todo.common.Constant
import kotlinx.android.synthetic.main.fragment_me.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by feiyang on 2018/8/13 14:17
 * Description : 我的
 */
class MeFragment : BaseFragment() {

    companion object {
        fun newInstance(): MeFragment {
            return MeFragment()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_me

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mAccInfoTv.text = "账户信息: ${SPUtils.getValue(Constant.USERNAME, "")}"

        mExitCardView.setOnClickListener {
            SPUtils.clear()
            startActivity<LoginActivity>()
            activity?.finish()
        }
    }

}