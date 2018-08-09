package com.xfhy.library.basekit.activity

import android.view.View
import android.widget.LinearLayout
import com.xfhy.library.basekit.presenter.AbstractPresenter
import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.presenter.RxPresenter
import com.xfhy.library.widgets.TitleBar
import kotlinx.android.synthetic.main.layout_header_bar.view.*
import org.jetbrains.anko.dip

/**
 * Created by xfhy on 2018/2/3 19:25
 * Description : 带标题栏的Activity MVP
 */
abstract class TitleBarMvpActivity<T : BasePresenter> : BaseMvpActivity<T>(), TitleBar.TitleBarListener {

    private lateinit var mTitleBar: TitleBar

    override fun setContentView(layoutResID: Int) {
        mTitleBar = TitleBar(this)
        mTitleBar.visibility = View.VISIBLE
        mTitleBar.setHeaderListener(this)
        val rootLayout = LinearLayout(this)
        rootLayout.orientation = LinearLayout.VERTICAL
        val paramView = View.inflate(this, layoutResID, null)
        rootLayout.addView(mTitleBar, LinearLayout.LayoutParams.MATCH_PARENT, dip(50))
        rootLayout.addView(paramView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        setContentView(rootLayout)
    }

    override fun onBackClick() {
        finish()
    }

    override fun onRightClick() {

    }

    /**
     * 设置标题
     */
    override fun setTitle(title: CharSequence) {
        mTitleBar.setTitleText(title.toString())
    }

    /**
     * 设置右侧按钮是否可见
     */
    fun setRightTvVisible(isShow: Boolean) {
        mTitleBar.mRightTv.visibility = if (isShow) View.VISIBLE else View.GONE
    }

}