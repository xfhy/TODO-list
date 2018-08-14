package com.xfhy.library.utils


import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.xfhy.library.R

/**
 * author xfhy
 * create at 2017/10/12 16:57
 * description：SnakeBar的简单封装,方便使用
 *
 *
 *
 *
 *
 *
 * 源码中:
 * Snackbar snackbar = new Snackbar(findSuitableParent(view));
 * Snackbar的第一个参数:view
 *
 *
 * findSuitableParent(view)方法的主要作用:
 * 1.当传入的View不为空时，如果我们在布局中发现了CoordinatorLayout布局，那么返回的View就是CoordinatorLayout；
 * 2.如果没有CoordinatorLayout布局，我们就先找到一个id为android.R.id.content的FrameLayout（这个布局是最底层的根布局），
 * 将View设置为该FrameLayout；
 * 3.其他情况下就使用View的Parent布局一直到这个View不为空。
 *
 *
 * 这里的颜色配置不能再R.color中配置  Snackbar里面是只能是@ColorInt来设置背景颜色
 */
object SnackbarUtil {

    /**
     * 信息类型
     */
    const val INFO = 1
    /**
     * 确认信息类型
     */
    const val CONFIRM = 2
    /**
     * 警告类型
     */
    const val WARNING = 3
    /**
     * 错误类型
     */
    const val ALERT = 4

    /**
     * 信息类型的背景颜色
     */
    private const val BLUE = -0xde6a0d
    /**
     * 确认信息类型背景颜色
     */
    private const val GREEN = -0xb350b0
    /**
     * 警告类型背景颜色
     */
    private const val ORANGE = -0x3ef9
    /**
     * 错误类型背景颜色
     */
    private const val RED = -0xbbcca
    /**
     * action文本颜色  白色
     */
    private const val WHITE = -0x1

    /**
     * 消息类型   替代Java中的枚举类型
     */
    @IntDef(INFO, CONFIRM, WARNING, ALERT)
    private annotation class MessageType

    /**
     * 显示Snackbar,时长:短时间(1570ms)，可自定义颜色
     *
     * @param view            The view to find a parent from.   view不能为空,
     * 否则会抛出IllegalArgumentException("No suitable parent found from the
     * given view.Please provide a valid view.");
     * @param message         需要显示的消息
     * @param messageColor    消息文本颜色
     * @param backgroundColor 背景颜色
     */
    fun showBarShortTime(view: View, message: String, @ColorInt messageColor: Int = WHITE, @ColorInt
    backgroundColor: Int = GREEN) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        setSnackbarColor(snackbar, messageColor, backgroundColor)

        snackbar.show()
    }

    /**
     * 显示Snackbar,时长:长时间(2750ms)，可自定义颜色
     *
     * @param view            The view to find a parent from.
     * @param message         需要显示的消息
     * @param messageColor    消息文本颜色
     * @param backgroundColor 背景颜色
     */
    fun showBarLongTime(view: View, message: String, @ColorInt messageColor: Int, @ColorInt backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        setSnackbarColor(snackbar, messageColor, backgroundColor)
        snackbar.show()
    }

    /**
     * 自定义时常显示Snackbar，自定义颜色
     *
     * @param view            The view to find a parent from.
     * @param message         需要显示的消息
     * @param duration        显示时长   单位:ms
     * @param messageColor    消息文本颜色
     * @param backgroundColor 背景颜色
     */
    fun showCustomCATSnackbar(view: View, message: String,
                              @IntRange(from = 1) duration: Int,
                              @ColorInt messageColor: Int,
                              @ColorInt backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setDuration(duration)
        setSnackbarColor(snackbar, messageColor, backgroundColor)
        snackbar.show()
    }

    /**
     * 显示Snackbar,时长:短时间(1570ms)，可选预设类型
     * android.support.design.widget.SnackbarManager.SHORT_DURATION_MS
     *
     * @param view    The view to find a parent from.
     * @param message 需要显示的消息
     * @param type    需要显示的消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    fun showBarShortTime(view: View, message: String, @MessageType
    type: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        switchType(snackbar, type)
        snackbar.show()
    }

    /**
     * 显示Snackbar,时长:短时间(1570ms)，可选预设类型
     * android.support.design.widget.SnackbarManager.SHORT_DURATION_MS
     *
     * @param view    The view to find a parent from.
     * @param message 需要显示的消息
     * @param type    需要显示的消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    fun showBarShortTime(view: View, message: String, @MessageType
    type: Int, text: CharSequence?, listener: View.OnClickListener) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(text,
                listener).setActionTextColor(WHITE)
        switchType(snackbar, type)
        snackbar.show()
    }

    /**
     * 显示Snackbar,时长:长时间(2750ms)，可选预设类型
     * android.support.design.widget.SnackbarManager.LONG_DURATION_MS
     *
     * @param view    The view to find a parent from.
     * @param message 需要显示的消息
     * @param type    需要显示的消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    @JvmStatic
    fun showBarLongTime(view: View, message: String, @MessageType type: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        switchType(snackbar, type)
        snackbar.show()
    }

    /**
     * 显示Snackbar,时长:长时间(2750ms)，可选预设类型
     * android.support.design.widget.SnackbarManager.LONG_DURATION_MS
     *
     * @param view    The view to find a parent from.
     * @param message 需要显示的消息
     * @param type    需要显示的消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    fun showBarLongTime(view: View, message: String, @MessageType
    type: Int = SnackbarUtil.INFO, text: CharSequence?, listener: View.OnClickListener) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(text,
                listener).setActionTextColor(WHITE)
        switchType(snackbar, type)
        snackbar.show()
    }

    /**
     * 自定义时长 显示Snackbar，可选预设类型
     *
     * @param view     The view to find a parent from.
     * @param message  需要显示的消息
     * @param duration 显示时长   单位:ms
     * @param type     需要显示的消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    fun showCustomTimeSnackbar(view: View, message: String, duration: Int, @MessageType type: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setDuration(duration)
        switchType(snackbar, type)
        snackbar.show()
    }

    /**
     * 设置Snackbar背景颜色
     *
     * @param snackbar        Snackbar
     * @param backgroundColor 背景颜色
     */
    private fun setSnackbarBgColor(snackbar: Snackbar, @ColorInt backgroundColor: Int) {
        val view = snackbar.view
        view.setBackgroundColor(backgroundColor)
    }

    /**
     * 设置Snackbar文字和背景颜色
     *
     * @param snackbar        Snackbar
     * @param messageColor    文字颜色
     * @param backgroundColor 背景颜色
     */
    private fun setSnackbarColor(snackbar: Snackbar, @ColorInt messageColor: Int,
                                 @ColorInt backgroundColor: Int) {
        val view = snackbar.view  //获取Snackbar自己的布局
        //设置Snackbar自己的布局的背景颜色
        view.setBackgroundColor(backgroundColor)
        //设置Snackbar自己的布局中的TextView的颜色
        (view.findViewById<View>(R.id.snackbar_text) as TextView).setTextColor(messageColor)
    }

    /**
     * 切换预设消息类型
     *
     * @param snackbar Snackbar
     * @param type     消息类型 SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    private fun switchType(snackbar: Snackbar, @MessageType type: Int) {
        when (type) {
            INFO -> setSnackbarBgColor(snackbar, BLUE)
            CONFIRM -> setSnackbarBgColor(snackbar, GREEN)
            WARNING -> setSnackbarBgColor(snackbar, ORANGE)
            ALERT -> setSnackbarColor(snackbar, Color.YELLOW, RED)
        }
    }

    /**
     * 向Snackbar中添加view
     *
     * @param snackbar Snackbar
     * @param layoutId 需要添加的布局的id
     * @param index    新加布局在Snackbar中的位置
     */
    fun SnackbarAddView(snackbar: Snackbar, @LayoutRes layoutId: Int, index: Int) {
        val snackbarView = snackbar.view
        val snackbarLayout = snackbarView as Snackbar.SnackbarLayout

        val addView = LayoutInflater.from(snackbarView.getContext()).inflate(layoutId, null)

        val p = LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        p.gravity = Gravity.CENTER_VERTICAL
        snackbarLayout.addView(addView, index, p)
    }

}
