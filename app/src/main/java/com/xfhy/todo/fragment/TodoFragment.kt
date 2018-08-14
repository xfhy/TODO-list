package com.xfhy.todo.fragment

import android.os.Bundle
import android.view.View
import com.xfhy.library.basekit.fragment.BaseMvpFragment
import com.xfhy.library.utils.SnackbarUtil
import com.xfhy.todo.R
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.TodoFragmentContract
import com.xfhy.todo.presenter.impl.TodoFragmentPresenter
import kotlinx.android.synthetic.main.fragment_todo_list.*

/**
 * Created by feiyang on 2018/8/13 11:41
 * Description : 未完成
 */
class TodoFragment : BaseMvpFragment<TodoFragmentContract.Presenter>(), TodoFragmentContract.View {

    companion object {
        fun newInstance(): TodoFragment {
            return TodoFragment()
        }
    }

    override fun initPresenter() {
        mPresenter = TodoFragmentPresenter(this)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_todo_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mPresenter?.getUndoneTodoList()
    }

    private fun initView() {
        mToolbar.title = "TODO"

        fab_add_todo.setOnClickListener {
            SnackbarUtil.showBarShortTime(fab_add_todo, "添加", SnackbarUtil.INFO)
        }
    }

    override fun showTodoList(todoData: TodoBean.Data) {

    }

    override fun showTips(tips: String) {
        SnackbarUtil.showBarShortTime(fab_add_todo, tips, SnackbarUtil.INFO)
    }
}