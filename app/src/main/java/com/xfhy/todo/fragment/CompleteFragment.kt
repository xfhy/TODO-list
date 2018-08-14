package com.xfhy.todo.fragment

import android.os.Bundle
import android.view.View
import com.xfhy.library.basekit.fragment.BaseMvpFragment
import com.xfhy.library.utils.SnackbarUtil
import com.xfhy.todo.R
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.CompleteFragmentContract
import com.xfhy.todo.presenter.TodoFragmentContract
import com.xfhy.todo.presenter.impl.CompleteFragmentPresenter
import com.xfhy.todo.presenter.impl.TodoFragmentPresenter
import kotlinx.android.synthetic.main.fragment_todo_list.*

/**
 * Created by feiyang on 2018/8/13 11:41
 * Description : 已完成清单
 */
class CompleteFragment : BaseMvpFragment<CompleteFragmentContract.Presenter>(), CompleteFragmentContract.View {

    companion object {
        fun newInstance(): CompleteFragment {
            return CompleteFragment()
        }
    }

    override fun initPresenter() {
        mPresenter = CompleteFragmentPresenter(this)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_complete_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mPresenter?.getDoneTodoList()
    }

    private fun initView() {

    }

    override fun showTodoList(todoData: TodoBean.Data) {

    }

    override fun showTips(tips: String) {

        //SnackbarUtil.showBarShortTime(mRootView, tips, SnackbarUtil.INFO)
    }
}