package com.xfhy.library.widgets

import android.content.Context
import android.support.annotation.AnimRes
import android.support.annotation.StringRes
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import com.xfhy.library.R

/**
 *
 * 多种状态布局
 */
class StatefulLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs, 0) {

    /**
     * 指示是否将动画置于状态更改中
     */
    var isAnimationEnabled: Boolean = false
    /**
     * 进入时动画
     */
    var inAnimation: Animation? = null
    /**
     * 离开界面时动画
     */
    var outAnimation: Animation? = null
    /**
     * 当动画持续时间短于状态请求时，同步转换动画
     *       改变
     */
    private var animCounter: Int = 0

    /**
     * 包裹的内容View
     */
    private var content: View? = null
    /**
     * 多样式布局的根布局
     */
    private var stContainer: LinearLayout? = null
    private var stProgress: ProgressBar? = null
    private var stImage: ImageView? = null
    private var stMessage: TextView? = null
    private var stButton: Button? = null

    init {

        //读取自定义属性
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable
                .stfStatefulLayout, 0, 0)
        isAnimationEnabled = array.getBoolean(R.styleable.stfStatefulLayout_stfAnimationEnabled,
                DEFAULT_ANIM_ENABLED)
        inAnimation = loadAnim(array.getResourceId(R.styleable.stfStatefulLayout_stfInAnimation,
                DEFAULT_IN_ANIM))
        outAnimation = loadAnim(array.getResourceId(R.styleable.stfStatefulLayout_stfOutAnimation,
                DEFAULT_OUT_ANIM))
        array.recycle()
    }

    fun setInAnimation(@AnimRes anim: Int) {
        inAnimation = loadAnim(anim)
    }

    fun setOutAnimation(@AnimRes anim: Int) {
        outAnimation = loadAnim(anim)
    }

    //从xml加载view已经完成会回调此方法
    override fun onFinishInflate() {
        super.onFinishInflate()

        //将自己写的样式布局加载到当前View中,并fbc

        if (childCount > 1) {
            throw IllegalStateException(MSG_ONE_CHILD)
        }
        if (isInEditMode) {  //判断当前视图是否可编辑
            return  // hide state views in designer
        }
        orientation = LinearLayout.VERTICAL
        content = getChildAt(0) // assume first child as content  Content是第一个
        LayoutInflater.from(context).inflate(R.layout.stf_template, this, true)
        stContainer = findViewById<View>(R.id.stContainer) as LinearLayout
        stProgress = findViewById<View>(R.id.stProgress) as ProgressBar
        stImage = findViewById<View>(R.id.stImage) as ImageView
        stMessage = findViewById<View>(R.id.stMessage) as TextView
        stButton = findViewById<View>(R.id.stButton) as Button
    }

    // content //

    fun showContent() {
        // 判断是否启用了动画
        if (isAnimationEnabled) {
            stContainer?.clearAnimation()  //取消此view的所有动画
            content?.clearAnimation()
            val animCounterCopy = ++animCounter
            if (stContainer?.visibility == View.VISIBLE) {
                outAnimation?.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounter != animCounterCopy) {
                            return
                        }
                        // 多样式布局隐藏,内容区可见
                        stContainer?.visibility = View.GONE
                        content?.visibility = View.VISIBLE
                        content?.startAnimation(inAnimation)
                    }
                })
                stContainer?.startAnimation(outAnimation)
            }
        } else {
            // 多样式布局隐藏,内容区可见
            stContainer?.visibility = View.GONE
            content?.visibility = View.VISIBLE
        }
    }

    @JvmOverloads
    fun showLoading(@StringRes resId: Int = R.string.stfLayoutLoadingMessage) {
        showLoading(getStringRes(resId))
    }

    fun showLoading(message: String) {
        showCustom(CustomStateOptions()
                .message(message)
                .loading())
    }

    @JvmOverloads
    fun showEmpty(@StringRes resId: Int = R.string.stfLayoutEmptyMessage) {
        showEmpty(getStringRes(resId))
    }

    fun showEmpty(message: String) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_empty))
    }

    fun showEmpty(message: String, stfButtonText: String) {
        showCustom(CustomStateOptions()
                .message(message)
                .buttonText(stfButtonText)
                .image(R.drawable.stf_ic_empty))
    }

    fun showEmpty(@StringRes textResId: Int, @StringRes btnTextResId: Int) {
        val image = CustomStateOptions()
                .message(getStringRes(textResId))
                .buttonText(getStringRes(btnTextResId))
                .image(R.drawable.stf_ic_empty)
        image.buttonText
        showCustom(image)
    }

    // error //

    fun showError(clickListener: View.OnClickListener) {
        showError(R.string.stfLayoutErrorMessage, clickListener)
    }

    fun showError(@StringRes resId: Int, clickListener: View.OnClickListener) {
        showError(getStringRes(resId), clickListener)
    }

    fun showError(message: String, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_error)
                .buttonText(getStringRes(R.string.stfLayoutButtonRetry))
                .buttonClickListener(clickListener))
    }

    fun showError(message: String, stfButtonText: String, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_error)
                .buttonText(stfButtonText)
                .buttonClickListener(clickListener))
    }

    fun showError(@StringRes textResId: Int, @StringRes btnTextResId: Int, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(getStringRes(textResId))
                .image(R.drawable.stf_ic_error)
                .buttonText(getStringRes(btnTextResId))
                .buttonClickListener(clickListener))
    }

    // offline

    fun showOffline(clickListener: View.OnClickListener) {
        showOffline(R.string.stfLayoutOfflineMessage, clickListener)
    }

    fun showOffline(@StringRes resId: Int, clickListener: View.OnClickListener) {
        showOffline(getStringRes(resId), clickListener)
    }

    fun showOffline(message: String, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_offline)
                .buttonText(getStringRes(R.string.stfLayoutButtonRetry))
                .buttonClickListener(clickListener))
    }

    fun showOffline(message: String, btnText: String, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_offline)
                .buttonText(btnText)
                .buttonClickListener(clickListener))
    }

    fun showOffline(@StringRes textResId: Int, @StringRes
    btnTextResId: Int, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(getStringRes(textResId))
                .image(R.drawable.stf_ic_offline)
                .buttonText(getStringRes(btnTextResId))
                .buttonClickListener(clickListener))
    }

    /**
     * Shows custom state for given options. If you do not set buttonClickListener, the button
     * will not be shown
     *
     * @param options customization options
     */
    fun showCustom(options: CustomStateOptions) {
        //启用动画了的
        if (isAnimationEnabled) {
            stContainer?.clearAnimation()
            content?.clearAnimation()
            val animCounterCopy = ++animCounter

            //如果多样式布局不可见
            if (stContainer?.visibility == View.GONE) {
                outAnimation?.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounterCopy != animCounter) {
                            return
                        }
                        content?.visibility = View.GONE
                        stContainer?.visibility = View.VISIBLE
                        stContainer?.startAnimation(inAnimation)
                    }
                })
                content?.startAnimation(outAnimation)
                state(options)
            } else {
                outAnimation?.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounterCopy != animCounter) {
                            return
                        }
                        state(options)
                        stContainer?.startAnimation(inAnimation)
                    }
                })
                stContainer?.startAnimation(outAnimation)
            }
        } else {
            content?.visibility = View.GONE
            stContainer?.visibility = View.VISIBLE
            state(options)
        }
    }

    /**
     * 根据状态model配置当前的界面应该显示什么
     */
    private fun state(options: CustomStateOptions) {
        if (!TextUtils.isEmpty(options.message)) {
            stMessage?.visibility = View.VISIBLE
            stMessage?.text = options.message
        } else {
            stMessage?.visibility = View.GONE
        }

        if (options.isLoading) {
            stProgress?.visibility = View.VISIBLE
            stImage?.visibility = View.GONE
            stButton?.visibility = View.GONE
        } else {
            stProgress?.visibility = View.GONE
            if (options.imageRes != 0) {
                stImage?.visibility = View.VISIBLE
                stImage?.setImageResource(options.imageRes)
            } else {
                stImage?.visibility = View.GONE
            }

            if (options.clickListener != null) {
                stButton?.visibility = View.VISIBLE
                stButton?.setOnClickListener(options.clickListener)
                if (!TextUtils.isEmpty(options.buttonText)) {
                    stButton?.text = options.buttonText
                }
            } else {
                stButton?.visibility = View.GONE
            }
        }
    }

    private fun getStringRes(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    private fun loadAnim(@AnimRes resId: Int): Animation {
        return AnimationUtils.loadAnimation(context, resId)
    }

    companion object {

        private val MSG_ONE_CHILD = "StatefulLayout must have one child!"
        private val DEFAULT_ANIM_ENABLED = true
        private val DEFAULT_IN_ANIM = android.R.anim.fade_in
        private val DEFAULT_OUT_ANIM = android.R.anim.fade_out
    }

}
