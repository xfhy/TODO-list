package com.xfhy.todo.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xfhy.library.adapter.BaseQuickAdapter
import com.xfhy.library.adapter.BaseViewHolder
import com.xfhy.library.basekit.fragment.BaseMvpFragment
import com.xfhy.library.utils.SnackbarUtil
import com.xfhy.todo.R
import com.xfhy.todo.activity.EditTodoActivity
import com.xfhy.todo.adapter.TodoAdapter
import com.xfhy.todo.common.Constant
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
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, TodoAdapter.OnClickListener {

    companion object {
        fun newInstance(): TodoFragment {
            return TodoFragment()
        }

        const val EDIT_REQUEST_CODE = 1003
        const val ADD_REQUEST_CODE = 1004
    }

    private var mTodoAdapter: TodoAdapter? = null
    /**
     * 当前正在编辑的item 的position
     */
    private var mEditPosition = -1

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

        //adapter
        mTodoAdapter = TodoAdapter(null,false)
        mTodoAdapter?.openLoadAnimation()
        mTodoAdapter?.isFirstOnly(false)
        mTodoAdapter?.setEnableLoadMore(true)
        val linearLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.adapter = mTodoAdapter

        mTodoAdapter?.onItemClickListener = this
        mTodoAdapter?.setOnClickListener(this)
        mRefreshLayout.setOnRefreshListener(this)
        mTodoAdapter?.setOnLoadMoreListener(this, mRecyclerView)

        //添加按钮
        fab_add_todo.setOnClickListener {
            EditTodoActivity.enterAddTodoActivity(this, ADD_REQUEST_CODE)
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

    override fun hideLoading() {
        super.hideLoading()
        mRefreshLayout.isRefreshing = false
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
        //这里还需要记录传过去的position  用于待会儿更改这边的数据
        mEditPosition = position
        EditTodoActivity.enterEditTodoActivity(this, adapter.getItem(position), EDIT_REQUEST_CODE)
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

    override fun doneTodoItem(position: Int) {
        val todoData = mTodoAdapter?.data
        todoData ?: return
        //标记网络数据
        mPresenter?.markTodoStatus(todoData[position].id, Constant.DONE_STATES)

        removeItem(position, todoData)
    }

    override fun deleteTodoItem(position: Int) {
        val todoData = mTodoAdapter?.data
        todoData ?: return

        //删除网络数据
        mPresenter?.deleteTodoById(mTodoAdapter?.data?.get(position)?.id ?: 0)
        removeItem(position, todoData)
    }

    private fun removeItem(position: Int, todoData: MutableList<TodoBean.Data.TodoItem>) {
        if (position < 0 || position >= todoData.size) {
            return
        }
        mTodoAdapter?.removeItem(position)
        if (todoData.size == 1) {
            //上一个是header
            if (todoData[position - 1].isHeader) {
                mTodoAdapter?.removeItem(position - 1)
            }
        } else if (todoData.size >= 2) {
            //上一个是header 下一个是header
            if (todoData[position - 1].isHeader && position < todoData.size && todoData[position].isHeader) {
                mTodoAdapter?.removeItem(position - 1)
            }
        }

        //如果最后一个是header  则删除末尾
        if (todoData.isNotEmpty() && todoData.last().isHeader) {
            mTodoAdapter?.removeItem(todoData.size - 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            EDIT_REQUEST_CODE -> {
                //记录传回来的值  更改adapter中对应position的值
                if (mEditPosition == -1) {
                    return
                }
                val adapterDataList = mTodoAdapter?.data
                adapterDataList ?: return
                data ?: return
                if (mEditPosition < 0 || mEditPosition >= adapterDataList.size) {
                    return
                }

                //找出更改过的item 更新RecyclerView
                val bundle = data.extras
                val todoItem = bundle.getSerializable(EditTodoActivity.EDIT_TODO_ITEM)
                if (todoItem is TodoBean.Data.TodoItem) {
                    adapterDataList[mEditPosition] = todoItem
                    mTodoAdapter?.notifyItemChanged(mEditPosition)
                }
            }
            ADD_REQUEST_CODE -> {
                onRefresh()
            }
            else -> {
            }
        }
    }

}