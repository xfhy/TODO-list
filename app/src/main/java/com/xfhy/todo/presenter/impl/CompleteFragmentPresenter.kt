package com.xfhy.todo.presenter.impl

import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.library.data.bean.BaseResp
import com.xfhy.library.rx.CommonSubscriber
import com.xfhy.library.rx.scheduler.SchedulerUtils
import com.xfhy.todo.data.TodoDataManager
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.CompleteFragmentContract
import com.xfhy.todo.presenter.TodoFragmentContract
import io.reactivex.Flowable

/**
 * Created by feiyang on 2018年8月13日11:52:46
 * Description : todo列表
 */
class CompleteFragmentPresenter(private val mView: CompleteFragmentContract.View) : RxPresenter(), CompleteFragmentContract.Presenter {

    companion object {
        //类别
        const val ZERO = 0
    }

    var mUndonePage = 1

    override fun getDoneTodoList() {
        //分页
        mView.showLoading()
        addSubscribe(TodoDataManager.getDoneTodoList(ZERO, mUndonePage).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<TodoBean>(mView, "获取TODO失败") {
                    override fun onNext(t: TodoBean?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            mView.showTodoList(t.data)
                        }
                    }
                }))
    }

    override fun markTodoStatus(id: Int, status: Int) {
        addSubscribe(TodoDataManager.markTodoStatus(id, status).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<BaseResp<String>>(mView, "标记失败") {
                    override fun onNext(t: BaseResp<String>?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            mView.showTips("已完成")
                        }
                    }
                }))
    }

    override fun deleteTodoById(id: Int) {
        addSubscribe(TodoDataManager.deleteTodoById(id).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<BaseResp<String>>(mView, "删除失败") {
                    override fun onNext(t: BaseResp<String>?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            mView.showTips("已删除")
                        }
                    }
                }))
    }

    override fun loadMoreData() {
        mUndonePage++
        getDoneTodoList()
    }
}