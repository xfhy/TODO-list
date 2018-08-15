package com.xfhy.todo.presenter.impl

import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.library.data.bean.BaseResp
import com.xfhy.library.rx.CommonSubscriber
import com.xfhy.library.rx.scheduler.SchedulerUtils
import com.xfhy.todo.data.TodoDataManager
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.EditTodoContract

/**
 * Created by feiyang on 2018/8/14 17:40
 * Description : 编辑 or 添加
 */
class EditTodoPresenter(val mView: EditTodoContract.View) : RxPresenter(), EditTodoContract.Presenter {
    override fun addTodo(title: String, content: String, date: String) {
        addSubscribe(TodoDataManager
                .addTodoById(title, content, date)
                .compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<BaseResp<TodoBean.Data.TodoItem>>(mView, "更新失败") {
                    override fun onNext(t: BaseResp<TodoBean.Data.TodoItem>?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            mView.addSuccess(t.data)
                        }
                    }
                }))
    }

    override fun update(id: Int, title: String, content: String, date: String, status: Int) {
        addSubscribe(TodoDataManager
                .updateTodoById(id, title, content, date, status)
                .compose(SchedulerUtils.ioToMain())
                .subscribeWith(object : CommonSubscriber<BaseResp<Any>>(mView, "更新失败") {
                    override fun onNext(t: BaseResp<Any>?) {
                        super.onNext(t)
                        if (t?.errorCode == 0) {
                            mView.updateSuccess()
                        }
                    }
                }))
    }
}