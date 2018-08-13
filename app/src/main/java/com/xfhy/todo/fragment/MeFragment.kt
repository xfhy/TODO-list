package com.xfhy.todo.fragment

import com.xfhy.library.basekit.fragment.BaseFragment
import com.xfhy.todo.R

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
}