package com.xfhy.todo.fragment

import com.xfhy.library.basekit.fragment.BaseMvpFragment
import com.xfhy.todo.R
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.TodoFragmentContract
import com.xfhy.todo.presenter.impl.TodoFragmentPresenter

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

    override fun showTodoList(todoList: TodoBean) {
    }
}