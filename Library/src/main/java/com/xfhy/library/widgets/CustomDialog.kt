package com.xfhy.library.widgets

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.xfhy.library.R
import com.xfhy.library.utils.DevicesUtils
import kotlinx.android.synthetic.main.layout_common_custom_dialog.*

/**
 * Created by xfhy on 2018/3/5 11:29
 * Description : 自定义的对话框
 */
class CustomDialog constructor(context: Context)
    : Dialog(context, R.style.CommonDialogTheme), View.OnClickListener {

    private var mTitle: String? = null
    private var mContent: String? = null
    private var mLeftBtnText: String? = null
    private var mRightBtnText: String? = null
    private var mListener: OnCustomDialogListener? = null

    constructor(context: Context, title: String? = null, content: String = "",
                leftBtnText: String? = null,
                rightBtnText: String = "",
                listener: OnCustomDialogListener? = null) : this(context) {
        mTitle = title
        mContent = content
        mLeftBtnText = leftBtnText
        mRightBtnText = rightBtnText
        mListener = listener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_common_custom_dialog)

        // 对话框属性设置
        val lp = window?.attributes
        lp?.width = DevicesUtils.devicesSize.widthPixels - 2 * 30 * DevicesUtils.devicesSize.density.toInt()
        lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp?.gravity = Gravity.CENTER
        window?.attributes = lp
        setCanceledOnTouchOutside(false)

        if (TextUtils.isEmpty(mTitle)) {
            mTitleTv.visibility = View.GONE
            mTitleLine.visibility = View.GONE
        } else {
            mTitleTv.text = mTitle
        }
        if (TextUtils.isEmpty(mLeftBtnText)) {
            mVerticalLine.visibility = View.GONE
            mLeftTv.visibility = View.GONE
        } else {
            mLeftTv.text = mLeftBtnText
        }

        mContentTv.text = mContent
        mRightTv.text = mRightBtnText

        mLeftTv.setOnClickListener(this)
        mRightTv.setOnClickListener(this)
    }

    /**
     * @param title
     * 设置标题
     */
    fun setTitle(title: String) {
        mTitleTv.text = title
    }

    /**
     * @param content
     * 设置显示内容
     */
    fun setContent(content: String) {
        mContentTv.text = content
    }

    /**
     * @param text
     * 设置左边标题
     */
    fun setLeftButtonText(text: String) {
        mLeftTv.text = text
    }

    /**
     * @param text
     * 设置右边标题
     */
    fun setRightButtonText(text: String) {
        mRightTv.text = text
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mLeftTv -> {
                mListener?.onLeftClick()
                dismiss()
            }
            R.id.mRightTv -> {
                mListener?.onRightClick()
                dismiss()
            }
            else -> {
            }
        }.let { dismiss() }
    }

    //监听器
    interface OnCustomDialogListener {
        fun onLeftClick()
        fun onRightClick()
    }

}