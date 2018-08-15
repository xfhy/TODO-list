package com.xfhy.todo.fragment

import android.os.Bundle
import android.view.View
import com.xfhy.library.basekit.fragment.BaseFragment
import com.xfhy.todo.R
import kotlinx.android.synthetic.main.fragment_tomato.*
import kotlinx.android.synthetic.main.layout_appbar.*

/**
 * Created by feiyang on 2018/8/13 14:17
 * Description : 番茄
 */
class TomatoFragment : BaseFragment() {
    companion object {
        fun newInstance(): TomatoFragment {
            return TomatoFragment()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_tomato

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        mToolbar.title = "番茄计时"
        var a = true
        tv_start_focus.setOnClickListener {
            if (a) {
                mTomatoView.startFocus()
            } else {
                mTomatoView.stopFocus()
            }
            a = !a
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTomatoView.stopFocus()
    }

}