package com.xfhy.todo.presenter.impl

import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.library.data.bean.BaseResp
import com.xfhy.library.rx.CommonSubscriber
import com.xfhy.library.rx.scheduler.SchedulerUtils
import com.xfhy.todo.common.Constant
import com.xfhy.todo.data.TodoDataManager
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.TodoFragmentContract
import io.reactivex.Flowable

/**
 * Created by feiyang on 2018年8月13日11:52:46
 * Description : todo列表
 */
class TodoFragmentPresenter(private val mView: TodoFragmentContract.View) : RxPresenter(), TodoFragmentContract.Presenter {

    var mUndonePage = 1

    override fun getUndoneTodoList() {
        //分页
        mView.showLoading()
        addSubscribe(TodoDataManager.getUndoneTodoList(Constant.TODO_TYPE, mUndonePage).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<TodoBean>(mView, "获取TODO失败") {
                    override fun onNext(t: TodoBean?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            //临时记录数据
                            val resultList = mutableListOf<TodoBean.Data.TodoItem>()
                            var lastDate = 0L
                            //将header组装起来
                            for (todo in t.data.todoList) {
                                if (lastDate == todo.date) {
                                    resultList.add(todo)
                                } else {
                                    resultList.add(TodoBean.Data.TodoItem(true, todo.dateStr))
                                    resultList.add(todo)
                                    lastDate = todo.date
                                }
                            }
                            mView.showTodoList(resultList)
                        } else {
                            mView.showEmptyView()
                        }
                    }
                }))

    }

    override fun markTodoStatus(id: Int, status: Int) {
        addSubscribe(TodoDataManager.markTodoStatus(id, status).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<BaseResp<Any>>(mView, "标记失败") {
                    override fun onNext(t: BaseResp<Any>?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            mView.showTips("已完成")
                        }
                    }
                }))
    }

    override fun deleteTodoById(id: Int) {
        addSubscribe(TodoDataManager.deleteTodoById(id).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<BaseResp<Any>>(mView, "删除失败") {
                    override fun onNext(t: BaseResp<Any>?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            mView.showTips("已删除")
                        }
                    }
                }))
    }

    /**
     * 加载更多数据
     * @param lastDateL 上一次最后的那个时间节点
     */
    override fun loadMoreData(lastDateL: Long) {
        mUndonePage++
        addSubscribe(TodoDataManager.getUndoneTodoList(Constant.TODO_TYPE, mUndonePage).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<TodoBean>(mView, "获取TODO失败") {
                    override fun onNext(t: TodoBean?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            //临时记录数据
                            val resultList = mutableListOf<TodoBean.Data.TodoItem>()
                            var lastDate = lastDateL
                            //将header组装起来
                            for (todo in t.data.todoList) {
                                if (lastDate == todo.date) {
                                    resultList.add(todo)
                                } else {
                                    resultList.add(TodoBean.Data.TodoItem(true, todo.dateStr))
                                    resultList.add(todo)
                                    lastDate = todo.date
                                }
                            }
                            mView.loadMoreSuccess(resultList)
                        } else {
                            mView.loadMoreFailed()
                        }
                    }
                }))
    }

    override fun onRefresh() {
        mUndonePage = 1
        //分页
        mView.showLoading()
        addSubscribe(TodoDataManager.getUndoneTodoList(Constant.TODO_TYPE, mUndonePage).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<TodoBean>(mView, "获取TODO失败") {
                    override fun onNext(t: TodoBean?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            val resultList = mutableListOf<TodoBean.Data.TodoItem>()
                            var lastDate = 0L
                            for (todo in t.data.todoList) {
                                if (lastDate == todo.date) {
                                    resultList.add(todo)
                                } else {
                                    resultList.add(TodoBean.Data.TodoItem(true, todo.dateStr))
                                    resultList.add(todo)
                                    lastDate = todo.date
                                }
                            }
                            mView.onRefresh(resultList)
                        } else {
                            mView.showEmptyView()
                        }
                    }
                }))
    }
}