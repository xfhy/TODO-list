package com.xfhy.todo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfhy.library.basekit.activity.TitleBarMvpActivity
import com.xfhy.todo.R
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.EditTodoContract
import com.xfhy.todo.presenter.impl.EditTodoPresenter
import kotlinx.android.synthetic.main.activity_edit_todo.*

/**
 * 2018年8月14日17:33:10
 * @author xfhy
 * 编辑todo界面
 */
class EditTodoActivity : TitleBarMvpActivity<EditTodoPresenter>(), EditTodoContract.View {

    companion object {
        private const val FUN_TYPE = "fun_type"
        private const val TODO_DATA = "todo_data"
        private const val FUN_TYPE_EDIT = 1000
        private const val FUN_TYPE_ADD = 1001

        /**
         * 进入编辑todo界面
         */
        fun enterEditTodoActivity(context: Context?, todoData: TodoBean.Data.TodoItem?) {
            val intent = Intent(context, EditTodoActivity::class.java)
            intent.putExtra(FUN_TYPE, FUN_TYPE_EDIT)
            val bundle = Bundle()
            bundle.putSerializable(TODO_DATA, todoData)
            intent.putExtras(bundle)
            context?.startActivity(intent)
        }

        /**
         * 进入添加todo界面
         */
        fun enterAddTodoActivity(context: Context?) {
            val intent = Intent(context, EditTodoActivity::class.java)
            intent.putExtra(FUN_TYPE, FUN_TYPE_ADD)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        initView()
        initIntentData()
    }

    private fun initIntentData() {
        val funType = intent.getIntExtra(FUN_TYPE, FUN_TYPE_EDIT)
        if (funType == FUN_TYPE_EDIT) {
            title = "清单详细"
            val bundle = intent.extras
            val todoData = bundle.getSerializable(TODO_DATA)
        } else if (funType == FUN_TYPE_ADD) {
            title = "添加清单"
        }
    }

    private fun initView() {
        animation_view.setOnClickListener{
            animation_view.setAnimation(R.raw.checked_done)
            animation_view.playAnimation()
        }
    }

    override fun initPresenter() {
    }

    override fun addSuccess() {
    }

    override fun updateSuccess() {

    }
}
