package com.xfhy.library.widgets

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.xfhy.library.R
import kotlinx.android.synthetic.main.layout_header_bar.view.*

/**
 * Created by xfhy on 2018/2/3 17:25
 * Description : 通用的标题栏
 */
class TitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var mTitleBarListener: TitleBarListener? = null

    init {
        View.inflate(context, R.layout.layout_header_bar, this)
        initView()
    }

    private fun initView() {
        mBackIv.setOnClickListener {
            mTitleBarListener?.onBackClick()
        }
        mRightTv.setOnClickListener {
            mTitleBarListener?.onRightClick()
        }
    }

    /**
     * 监听标题栏的点击事件
     */
    fun setHeaderListener(listener: TitleBarListener) {
        mTitleBarListener = listener
    }

    /**
     * 设置标题
     */
    fun setTitleText(title: String) {
        mTitleTv.text = title
    }

    /**
     * 设置右侧文字
     */
    fun setRightText(rightText: String) {
        mRightTv.text = rightText
    }

    interface TitleBarListener {
        fun onBackClick()
        fun onRightClick()
    }

}