package com.xfhy.library.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.xfhy.library.R
import com.xfhy.library.utils.DensityUtil
import java.util.*

/**
 * Created by feiyang on 2018/8/15 15:35
 * Description : 番茄计数 View
 */
class TomatoView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defaultStyle: Int = 0) : View(context, attr, defaultStyle) {

    companion object {
        /**
         * View 的状态:开始 或者 结束
         */
        private const val STATUS_START = 1005
        private const val STATUS_STOP = 1006
        private const val DEFAULT_OUTER_RING_WIDTH = 4f
        const val DEFAULT_ALL_TIME = 1500000L
        /**
         * 倒计时速度 1秒
         */
        private const val PASS_TIME_DELAY_MILLIS = 1000L
        private const val DEFAULT_TIME_TEXT_SIZE = 160f
        private const val DEFAULT_WELCOME_TEXT_SIZE = 40f
    }

    /**
     * 外圈线  未经过路段
     */
    private val mOuterRingPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 外圈线  已经过路段
     */
    private val mOuterRingPassPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 时间
     */
    private val mTimePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 欢迎语
     */
    private val mWelcomePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mHeight = 0
    private var mWidth = 0
    /**
     * 中心点
     */
    private var mCenterX = 0f
    private var mCenterY = 0f
    /**
     * 半径
     */
    private var mRadius = 0f
    /**
     * 总时长 单位:s
     */
    var mAllTime = DEFAULT_ALL_TIME
    /**
     * 已经过去的时长
     */
    private var mPastTime = 0L
    /**
     * 当前状态
     */
    private var mCurrentStatus = STATUS_STOP
    private var mListener: TomatoListener? = null

    init {
        mOuterRingPaint.color = resources.getColor(R.color.color_e6e6e6)
        mOuterRingPaint.strokeWidth = DensityUtil.dip2px(context, DEFAULT_OUTER_RING_WIDTH).toFloat()
        //空心
        mOuterRingPaint.style = Paint.Style.STROKE

        mOuterRingPassPaint.color = resources.getColor(R.color.colorPrimary)
        mOuterRingPassPaint.strokeWidth = DensityUtil.dip2px(context, DEFAULT_OUTER_RING_WIDTH).toFloat()
        //空心
        mOuterRingPassPaint.style = Paint.Style.STROKE

        mTimePaint.color = resources.getColor(R.color.colorPrimary)
        mTimePaint.textSize = DEFAULT_TIME_TEXT_SIZE

        mWelcomePaint.color = resources.getColor(R.color.gray_cdcdcd)
        mWelcomePaint.textSize = DEFAULT_WELCOME_TEXT_SIZE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mRadius = if (mWidth > mHeight) {
            //半径=高度-线条宽度-10(不减10的话边界那里有点奇怪)
            (mHeight - DEFAULT_OUTER_RING_WIDTH * 2 - 10) / 2
        } else {
            (mWidth - DEFAULT_OUTER_RING_WIDTH * 2 - 10) / 2
        }
        mCenterX = (mWidth / 2).toFloat()
        mCenterY = (mHeight / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawOuterRing(canvas)
        drawTimeText(canvas)
        drawWelcomeText(canvas)
    }

    /**
     * 欢迎语绘画
     */
    private fun drawWelcomeText(canvas: Canvas?) {
        canvas ?: return
        if (mCurrentStatus == STATUS_START) {
            return
        }
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val welcomeText = when (hour) {
            in 0..12 -> {
                "早上好,准备好开始专注了吗?"
            }
            in 13..18 -> {
                "下午好,准备好开始专注了吗?"
            }
            in 19..23 -> {
                "晚上好,准备好开始专注了吗?"
            }
            else -> {
                "您好,准备好开始专注了吗?"
            }
        }

        //---------------计算时间的字体高度
        var ascent = mTimePaint.ascent()  //字体的顶部
        var descent = mTimePaint.descent() //字体的底部
        val timeTextHeight = ((descent - ascent) / 2 - descent) //字体高度

        //-----------------计算欢迎语的字体高度
        ascent = mWelcomePaint.ascent()  //字体的顶部
        descent = mWelcomePaint.descent() //字体的底部
        val welcomeTextHeight = ((descent - ascent) / 2 - descent) //字体高度
        val welcomeTextWidth = mWelcomePaint.measureText(welcomeText)

        canvas.drawText(welcomeText, mCenterX - welcomeTextWidth / 2,
                mCenterY + timeTextHeight / 2 + DensityUtil.dip2px(context, 20f) + welcomeTextHeight, mWelcomePaint)
    }

    /**
     * 时间绘画
     */
    private fun drawTimeText(canvas: Canvas?) {
        canvas ?: return

        val timeText: String
        val ascent = mTimePaint.ascent()  //字体的顶部
        val descent = mTimePaint.descent() //字体的底部
        val textHeight = ((descent - ascent) / 2 - descent) //字体高度
        if (mCurrentStatus == STATUS_STOP) {
            //结束状态 则画总时间
            val second = (mAllTime % 60000) / 1000
            timeText = "${mAllTime / 60000}:${if (second == 0L) "00" else second}"
            val textWidth = mTimePaint.measureText(timeText)
            canvas.drawText(timeText, mCenterX - textWidth / 2, mCenterY + textHeight / 2, mTimePaint)
            return
        }

        //画倒计时状态的时间
        val second = ((mAllTime - mPastTime) % 60000) / 1000
        timeText = "${(mAllTime - mPastTime) / 60000}:${if (second == 0L) "00" else second}"
        val textWidth = mTimePaint.measureText(timeText)
        canvas.drawText(timeText, mCenterX - textWidth / 2, mCenterY + textHeight / 2, mTimePaint)
    }

    /**
     * 外圈 绘画
     */
    private fun drawOuterRing(canvas: Canvas?) {
        canvas ?: return
        //判断当前状态是否结束 结束则不画外圈
        if (mCurrentStatus == STATUS_STOP) {
            return
        }
        //-----画全路段
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mOuterRingPaint)

        //-----画已经经过的路段  外圈
        val left = mCenterX - mRadius
        val right = mCenterX + mRadius
        val top = mCenterY - mRadius
        val bottom = mCenterY + mRadius
        val path = Path()
        val sweep = (mPastTime * 1.0 / mAllTime * 360).toFloat()
        path.addArc(left, top, right, bottom, -90f, sweep)
        canvas.drawPath(path, mOuterRingPassPaint)
    }

    private var timeTask: Runnable = object : Runnable {
        override fun run() {
            mPastTime += 1000
            //重绘
            invalidate()
            //延迟1秒 继续倒计时
            if (mPastTime != mAllTime && mCurrentStatus == STATUS_START) {
                handler.postDelayed(this, PASS_TIME_DELAY_MILLIS)
            } else {
                mCurrentStatus = STATUS_STOP
                if (mPastTime == mAllTime) {
                    mPastTime = 0
                    mListener?.onFinish()
                }
            }
        }
    }

    /**
     * 开始倒计时
     */
    fun startFocus() {
        mCurrentStatus = STATUS_START
        handler.postDelayed(timeTask, PASS_TIME_DELAY_MILLIS)
    }

    /**
     * 停止倒计时
     */
    fun stopFocus() {
        mPastTime = 0
        mCurrentStatus = STATUS_STOP
        handler.removeCallbacksAndMessages(timeTask)
    }

    /**
     * 设置监听器
     */
    fun setTomatoListener(listener: TomatoListener) {
        mListener = listener
    }

    interface TomatoListener {
        fun onFinish()
    }

}