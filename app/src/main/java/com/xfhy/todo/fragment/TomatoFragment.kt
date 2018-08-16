package com.xfhy.todo.fragment

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import com.xfhy.library.basekit.fragment.BaseFragment
import com.xfhy.library.utils.SPUtils
import com.xfhy.library.widgets.TomatoView
import com.xfhy.todo.R
import com.xfhy.todo.common.Constant
import kotlinx.android.synthetic.main.fragment_tomato.*
import kotlinx.android.synthetic.main.layout_appbar.*

/**
 * Created by feiyang on 2018/8/13 14:17
 * Description : 番茄
 */
class TomatoFragment : BaseFragment(), View.OnClickListener, TomatoView.TomatoListener {
    companion object {
        fun newInstance(): TomatoFragment {
            return TomatoFragment()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_tomato

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private var mIsFocus = false

    private fun initView() {
        mToolbar.title = "番茄计时"
        val tomatoCount = SPUtils.getValue(Constant.TOMATO_COUNT, 0)
        tv_tomato_count.text = "$tomatoCount"
        tv_focus_time.text = "${tomatoCount * TomatoView.DEFAULT_ALL_TIME / 3600000}"

        mTomatoView.setOnClickListener(this)
        mTomatoView.setTomatoListener(this)
        mTomatoInfoRl.setOnClickListener(this)
        mStartFocusTv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mStartFocusTv -> {
                mIsFocus = !mIsFocus
                if (mIsFocus) {
                    mTomatoView.startFocus()
                    mTomatoInfoRl.visibility = View.GONE
                    mStartFocusTv.setBackgroundResource(R.drawable.shape_solid_stop_focus)
                    mStartFocusTv.setTextColor(resources.getColor(R.color.colorPrimary))
                    mStartFocusTv.text = "放弃番茄"
                } else {
                    mTomatoView.stopFocus()
                    mStartFocusTv.setBackgroundResource(R.drawable.shape_solid_start_focus)
                    mStartFocusTv.setTextColor(resources.getColor(R.color.color_ffffff))
                    mStartFocusTv.text = "开始专注"
                }
            }
            R.id.mTomatoInfoRl -> {
                mTomatoView.visibility = View.VISIBLE
                mTomatoInfoRl.visibility = View.GONE
            }
            R.id.mTomatoView -> {
                mTomatoView.visibility = View.GONE
                mTomatoInfoRl.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }

    override fun onFinish() {
        //按钮设置为开始专注
        mStartFocusTv.setBackgroundResource(R.drawable.shape_solid_start_focus)
        mStartFocusTv.setTextColor(resources.getColor(R.color.color_ffffff))
        mStartFocusTv.text = "开始专注"
        //sp
        var tomatoCount = SPUtils.getValue(Constant.TOMATO_COUNT, 0)
        tomatoCount++
        SPUtils.putValue(Constant.TOMATO_COUNT, tomatoCount)
        tv_tomato_count.text = "$tomatoCount"
        tv_focus_time.text = "${tomatoCount * TomatoView.DEFAULT_ALL_TIME / 3600000}"

        //使用默认的声音  发送通知
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(context, Constant.CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("已完成番茄")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .build()
            manager?.notify(1, notification)
        } else {
            val notification = Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("已完成番茄")
                    .setDefaults(Notification.DEFAULT_SOUND).build()
            manager?.notify(1, notification)
        }
    }

}