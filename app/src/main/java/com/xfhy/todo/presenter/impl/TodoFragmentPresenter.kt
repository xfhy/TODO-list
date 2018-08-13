package com.xfhy.todo.presenter.impl

import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.library.rx.CommonSubscriber
import com.xfhy.library.rx.scheduler.SchedulerUtils
import com.xfhy.todo.data.TodoDataManager
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.TodoFragmentContract
import io.reactivex.Flowable

/**
 * Created by feiyang on 2018年8月13日11:52:46
 * Description : todo列表
 */
class TodoFragmentPresenter(private val mView: TodoFragmentContract.View) : RxPresenter(), TodoFragmentContract.Presenter {

    companion object {
        //不同级别的优先级类型
        const val ZERO = 0
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
    }

    var mUndonePage = 1
    override fun getUndoneTodoList() {
        val zeroFlowable = TodoDataManager.getUndoneTodoList(ZERO, mUndonePage)
        val oneFlowable = TodoDataManager.getUndoneTodoList(ONE, mUndonePage)
        val twoFlowable = TodoDataManager.getUndoneTodoList(TWO, mUndonePage)
        val threeFlowable = TodoDataManager.getUndoneTodoList(THREE, mUndonePage)
        //优先级 0,1,2,3
        //分页

        //服务端没有那种可以一次性返回所有优先级todo的接口   于是我把所有优先级的都请求回来,再进行展示
        addSubscribe(Flowable.concat(zeroFlowable, oneFlowable, twoFlowable, threeFlowable).compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<TodoBean>(mView, "获取TODO失败") {
                    override fun onNext(t: TodoBean?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            mView.showTodoList(t.data)
                        }
                    }
                }))

    }

    override fun getDoneTodoList() {
    }

    override fun markTodoStatus(id: Int, status: Int) {
    }

    override fun deleteTodoById(id: Int) {
    }
}