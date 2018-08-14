package com.xfhy.library.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.ColorLong
import android.support.annotation.DrawableRes
import android.support.annotation.FloatRange
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.util.SparseArray
import android.view.View
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView

/**
 * author xfhy
 * create at 2017/9/26 16:54
 * description：适用所有RecyclerView的ViewHolder
 */
open class BaseViewHolder//初始化ViewHolder
(//子布局
        private val mView: View) : RecyclerView.ViewHolder(mView) {

    //子布局中的控件
    private val mItemViews: SparseArray<View> = SparseArray()

    /**
     * 获取子控件
     *
     *
     * 子控件的id
     *
     * @param viewId 返回子控件
     * @return
     */
    fun getView(@IdRes viewId: Int): View? {
        var view: View? = mItemViews.get(viewId)
        if (view == null) {
            view = mView.findViewById(viewId)
            mItemViews.put(viewId, view)
        }
        return view
    }

    /**
     * 通过strings.xml文件给TextView设置文本
     *
     *
     * 子控件的id
     *
     * @param viewId 子控件在strings.xml中的文本
     * @param resId  返回子控件
     * @return BaseViewHolder
     */
    fun setText(@IdRes viewId: Int, @StringRes resId: Int): BaseViewHolder {
        val textView = getView(viewId) as TextView
        textView.setText(resId)
        return this
    }

    /**
     * 通过String给TextView设置文本
     *
     *
     * 子控件的id
     *
     * @param viewId 子控件中的文本
     * @param text   返回子控件
     * @return BaseViewHolder
     */
    fun setText(@IdRes viewId: Int, text: String?): BaseViewHolder {
        val textView = getView(viewId) as TextView
        if (text != null) {
            textView.text = text
        } else {
            textView.text = ""
        }
        return this
    }

    /**
     * 通过SpannableStringBuilder给TextView设置文本
     *
     * @param viewId View的id
     * @param text   文本
     * @return BaseViewHolder
     */
    fun setText(@IdRes viewId: Int, text: SpannableStringBuilder?): BaseViewHolder {
        val textView = getView(viewId) as TextView
        if (text != null) {
            textView.text = text
        } else {
            textView.text = ""
        }
        return this
    }

    /**
     * 通过drawable文件夹中的资源设置图片
     *
     * @param viewId view的id
     * @param resId  文本
     * @return BaseViewHolder
     */
    fun setImageResource(@IdRes viewId: Int, @DrawableRes resId: Int): BaseViewHolder {
        val imageView = getView(viewId) as ImageView
        imageView.setImageResource(resId)
        return this
    }

    /**
     * 通过Bitmap设置图片
     *
     * @param viewId view Id
     * @param bitmap Bitmap
     * @return BaseViewHolder
     */
    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): BaseViewHolder {
        val imageView = getView(viewId) as ImageView
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        }
        return this
    }

    /**
     * 通过Drawable设置图片
     *
     * @param viewId   View的id
     * @param drawable Drawable
     * @return BaseViewHolder
     */
    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable): BaseViewHolder {
        val imageView = getView(viewId) as ImageView
        if (drawable != null) {
            imageView.setImageDrawable(drawable)
        }
        return this
    }

    /**
     * 通过一串数字设置背景色
     *
     * @param viewId View的id
     * @param color  颜色值 16进制
     * @return BaseViewHolder
     */
    fun setBackgroundColor(@IdRes viewId: Int, @ColorLong color: Int): BaseViewHolder {
        val view = getView(viewId)
        view?.setBackgroundColor(color)
        return this
    }

    /**
     * 通过drawable文件夹设置背景图
     *
     * @param viewId        View的id
     * @param backgroundRes Resource
     * @return BaseViewHolder
     */
    fun setBackgroundResource(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): BaseViewHolder {
        val view = getView(viewId)
        view?.setBackgroundResource(backgroundRes)
        return this
    }


    /**
     * 通过Drawable设置背景图
     *
     * @param viewId   View的id
     * @param drawable Drawable
     * @return BaseViewHolder
     */
    fun setBackgroundDrawable(@IdRes viewId: Int, drawable: Drawable?): BaseViewHolder {
        val view = getView(viewId)
        if (drawable != null) {
            view?.background = drawable
        }
        return this
    }

    /**
     * 通过一串数字设置文字颜色
     *
     * @param viewId    View的id
     * @param textColor 颜色值 16进制
     * @return BaseViewHolder
     */
    fun setTextColor(@IdRes viewId: Int, @ColorLong textColor: Int): BaseViewHolder {
        val textView = getView(viewId) as TextView
        textView.setTextColor(textColor)
        return this
    }

    /**
     * 通过float设置透明度
     *
     * @param viewId View的id
     * @param value  透明度  范围:[0.0,1.0]
     * @return BaseViewHolder
     */
    fun setAlpha(@IdRes viewId: Int, @FloatRange(from = 0.0, to = 1.0) value: Float): BaseViewHolder {
        getView(viewId)?.alpha = value
        return this
    }

    /**
     * 通过boolean类型设置是否显示
     *
     * @param viewId  View的id
     * @param visible 是否可见 true:可见;  false:不可见,Gone
     * @return BaseViewHolder
     */
    fun setVisible(@IdRes viewId: Int, visible: Boolean): BaseViewHolder {
        val view = getView(viewId)
        view?.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    /**
     * 缓存子控件上界面的数据
     *
     * @param viewId View的id
     * @param tag    需要缓存的数据
     * @return BaseViewHolder
     */
    fun setTag(@IdRes viewId: Int, tag: Any): BaseViewHolder {
        val view = getView(viewId)
        view?.tag = tag
        return this
    }

    /**
     * 设置某一位置子控件的数据
     *
     * @param viewId View的id
     * @param key    数据标识
     * @param tag    数据
     * @return BaseViewHolder
     */
    fun setTag(@IdRes viewId: Int, key: Int, tag: Any): BaseViewHolder {
        val view = getView(viewId)
        view?.setTag(key, tag)
        return this
    }

    /**
     * 设置子控件是否选中
     *
     * @param viewId  View的id
     * @param checked true:选中   false:未选中
     * @return BaseViewHolder
     */
    fun setChecked(@IdRes viewId: Int, checked: Boolean): BaseViewHolder {
        val checkable = getView(viewId) as Checkable
        checkable.isChecked = checked
        return this
    }

    /**
     * 设置子控件的点击事件
     *
     * @param viewId   View的id
     * @param listener OnClickListener监听器
     * @return BaseViewHolder
     */
    fun setOnClickListener(@IdRes viewId: Int, listener: View.OnClickListener): BaseViewHolder {
        val view = getView(viewId)
        if (listener != null) {
            view?.setOnClickListener(listener)
        }
        return this
    }

    /**
     * 设置子控件的触摸事件
     *
     * @param viewId   View的id
     * @param listener OnTouchListener
     * @return BaseViewHolder
     */
    fun setOnTouchListener(@IdRes viewId: Int, listener: View.OnTouchListener): BaseViewHolder {
        val view = getView(viewId)
        if (listener != null) {
            view?.setOnTouchListener(listener)
        }
        return this
    }

    /**
     * 设置子控件的长按事件
     *
     * @param viewId   View的id
     * @param listener OnLongClickListener
     * @return BaseViewHolder
     */
    fun setOnLongClickListener(@IdRes viewId: Int, listener: View.OnLongClickListener): BaseViewHolder {
        val view = getView(viewId)
        if (listener != null) {
            view?.setOnLongClickListener(listener)
        }
        return this
    }
}
