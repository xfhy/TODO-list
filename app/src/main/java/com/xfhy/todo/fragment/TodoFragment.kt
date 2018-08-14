package com.xfhy.todo.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xfhy.library.adapter.BaseQuickAdapter
import com.xfhy.library.adapter.BaseViewHolder
import com.xfhy.library.basekit.fragment.BaseMvpFragment
import com.xfhy.library.utils.SnackbarUtil
import com.xfhy.todo.R
import com.xfhy.todo.adapter.TodoAdapter
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.TodoFragmentContract
import com.xfhy.todo.presenter.impl.TodoFragmentPresenter
import kotlinx.android.synthetic.main.fragment_todo_list.*

/**
 * Created by feiyang on 2018/8/13 11:41
 * Description : 未完成
 */
class TodoFragment : BaseMvpFragment<TodoFragmentContract.Presenter>(), TodoFragmentContract.View,
        BaseQuickAdapter.OnItemClickListener<TodoBean.Data.TodoItem, BaseViewHolder>,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance(): TodoFragment {
            return TodoFragment()
        }
    }

    private var mTodoAdapter: TodoAdapter? = null

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
        //下拉刷新颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        mTodoAdapter = TodoAdapter(null)
        mTodoAdapter?.openLoadAnimation()
        mTodoAdapter?.isFirstOnly(false)
        mTodoAdapter?.setEnableLoadMore(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = mTodoAdapter

        mTodoAdapter?.onItemClickListener = this
        mRefreshLayout.setOnRefreshListener(this)
        mTodoAdapter?.setOnLoadMoreListener(this, mRecyclerView)

        fab_add_todo.setOnClickListener {
            SnackbarUtil.showBarShortTime(fab_add_todo, "添加", SnackbarUtil.INFO)
        }
    }

    override fun showTodoList(todoData: MutableList<TodoBean.Data.TodoItem>) {
        mRefreshLayout.isRefreshing = false
        mTodoAdapter?.addData(todoData)
        mTodoAdapter?.disableLoadMoreIfNotFullPage()
    }

    override fun loadMoreSuccess(todoData: MutableList<TodoBean.Data.TodoItem>) {
        mTodoAdapter?.loadMoreComplete()
        mTodoAdapter?.addData(todoData)
        mTodoAdapter?.disableLoadMoreIfNotFullPage()
    }

    override fun loadMoreFailed() {
        mTodoAdapter?.loadMoreFail()
    }


    override fun onRefresh(todoData: MutableList<TodoBean.Data.TodoItem>) {
        mRefreshLayout.isRefreshing = false
        mTodoAdapter?.setNewData(todoData)
        mTodoAdapter?.disableLoadMoreIfNotFullPage()
    }

    override fun showTips(tips: String) {
        SnackbarUtil.showBarShortTime(fab_add_todo, tips, SnackbarUtil.INFO)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<TodoBean.Data.TodoItem, BaseViewHolder>, view: View, position: Int) {

    }

    override fun onLoadMoreRequested() {
        mTodoAdapter?.data?.let {
            if (it.isNotEmpty()) {
                mPresenter?.loadMoreData(it.last().date)
            }
        }
    }

    override fun onRefresh() {
        mPresenter?.onRefresh()
    }

}