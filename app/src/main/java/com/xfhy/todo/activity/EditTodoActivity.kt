package com.xfhy.todo.activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.xfhy.library.basekit.activity.TitleBarMvpActivity
import com.xfhy.library.utils.DateUtils
import com.xfhy.todo.R
import com.xfhy.todo.common.Constant
import com.xfhy.todo.data.bean.TodoBean
import com.xfhy.todo.presenter.EditTodoContract
import com.xfhy.todo.presenter.impl.EditTodoPresenter
import kotlinx.android.synthetic.main.activity_edit_todo.*
import org.jetbrains.anko.toast
import java.util.*

/**
 * 2018年8月14日17:33:10
 * @author xfhy
 * 编辑todo界面
 */
class EditTodoActivity : TitleBarMvpActivity<EditTodoPresenter>(), EditTodoContract.View {

    companion object {
        /**
         * 当前界面的功能类型:编辑 或者 添加
         */
        private const val FUN_TYPE = "fun_type"
        private const val TODO_DATA = "todo_data"
        private const val FUN_TYPE_EDIT = 1000
        private const val FUN_TYPE_ADD = 1001

        //用于Activity 回传数据
        const val EDIT_TODO_ITEM = "edit_todo_item"
        const val ADD_TODO_ITEM = "add_todo_item"

        /**
         * 进入编辑todo界面
         */
        fun enterEditTodoActivity(fragment: Fragment, todoData: TodoBean.Data.TodoItem?, requestCode: Int) {
            val intent = Intent(fragment.context, EditTodoActivity::class.java)
            intent.putExtra(FUN_TYPE, FUN_TYPE_EDIT)
            val bundle = Bundle()
            bundle.putSerializable(TODO_DATA, todoData)
            intent.putExtras(bundle)
            fragment.startActivityForResult(intent, requestCode)
        }

        /**
         * 进入添加todo界面
         */
        fun enterAddTodoActivity(fragment: Fragment, requestCode: Int) {
            val intent = Intent(fragment.context, EditTodoActivity::class.java)
            intent.putExtra(FUN_TYPE, FUN_TYPE_ADD)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    /**
     * 当前界面的功能类型:编辑 或者 添加
     */
    private var mFunType = FUN_TYPE_EDIT
    /**
     * 编辑todo时传入的值
     */
    private var mTodoItemData: TodoBean.Data.TodoItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        initIntentData()
        initView()
    }

    override fun initPresenter() {
        mPresenter = EditTodoPresenter(this)
    }

    private fun initIntentData() {
        mFunType = intent.getIntExtra(FUN_TYPE, FUN_TYPE_EDIT)
        if (mFunType == FUN_TYPE_EDIT) {
            title = "清单详细"
            val bundle = intent.extras
            mTodoItemData = bundle.getSerializable(TODO_DATA) as? TodoBean.Data.TodoItem
            mTitleEt.setText(mTodoItemData?.title ?: "")
            mContentEt.setText(mTodoItemData?.content ?: "")
            mChooseTimeTv.text = mTodoItemData?.dateStr ?: ""
        } else if (mFunType == FUN_TYPE_ADD) {
            title = "添加清单"
        }
    }

    private fun initView() {
        //只有在添加时才需要设置日期默认值
        if (mFunType == FUN_TYPE_ADD) {
            mChooseTimeTv.text = DateUtils.getDateFormatText(System.currentTimeMillis(), "yyyy-MM-dd")
        }

        mChooseTimeTv.setOnClickListener {
            showDatePickerDialog()
        }
        mOkAnimationView.setOnClickListener {
            determine()
        }
    }

    private fun determine() {
        //检测合法输入
        if (mTitleEt.text.toString().trim().isEmpty()) {
            titleEmptyWarn()
            return
        }

        mOkAnimationView.playAnimation()
        showLoading()
        when (mFunType) {
            FUN_TYPE_EDIT -> {
                //更新
                mPresenter?.update(mTodoItemData?.id
                        ?: 0, mTitleEt.text.toString().trim(), mContentEt.text.toString().trim(), mChooseTimeTv.text.toString(),
                        mTodoItemData?.status ?: Constant.UNDONE_STATES)
            }
            FUN_TYPE_ADD -> {
                mPresenter?.addTodo(mTitleEt.text.toString().trim(), mContentEt.text.toString().trim(), mChooseTimeTv.text.toString())
            }
            else -> {
            }
        }
    }

    private fun titleEmptyWarn() {
        //使mTitleEt上下抖动  加强用户注意
        mTitleEt.setHintTextColor(resources.getColor(R.color.snackbar_origin))
        val titleY = mTitleEt.rotationY
        val objectAnimator = ObjectAnimator.ofFloat(mTitleEt, "translationY",
                titleY + 5, titleY - 5, titleY + 5, titleY - 5, titleY + 5, titleY - 5, titleY + 5, titleY - 5, titleY)
        objectAnimator.duration = 1500
        objectAnimator.start()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mTitleEt.setHintTextColor(resources.getColor(R.color.gray_cdcdcd))
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        toast("请输入标题!!")
    }

    override fun addSuccess(data: TodoBean.Data.TodoItem?) {
        if (mFunType == FUN_TYPE_ADD) {
            //回传数据
            val intent = Intent()
            val bundle = Bundle()
            data?.let {
                bundle.putSerializable(EDIT_TODO_ITEM, data)
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    override fun updateSuccess() {
        if (mFunType == FUN_TYPE_EDIT) {
            //回传数据
            mTodoItemData?.title = mTitleEt.text.toString().trim()
            mTodoItemData?.content = mContentEt.text.toString().trim()
            mTodoItemData?.dateStr = mChooseTimeTv.text.toString()
            val intent = Intent()
            val bundle = Bundle()
            bundle.putSerializable(EDIT_TODO_ITEM, mTodoItemData)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    /**
     * 展示选择系统日期对话框
     */
    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            mChooseTimeTv.text = "$year-${month + 1}-$dayOfMonth"
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        val datePicker = datePickerDialog.datePicker

        //如果是添加todo 则设置一个开始日期为今天  这样才合理
        if (mFunType == FUN_TYPE_ADD) {
            //设置起始日期
            datePicker.minDate = System.currentTimeMillis()
        }
        //设置结束日期
        //datePicker.maxDate = xx
        datePickerDialog.show()
    }

}
