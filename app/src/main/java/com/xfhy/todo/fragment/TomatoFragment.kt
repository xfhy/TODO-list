package com.xfhy.todo.fragment

import com.xfhy.library.basekit.fragment.BaseFragment
import com.xfhy.todo.R

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
}