package com.xfhy.library.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.xfhy.library.R
import com.xfhy.library.utils.SoftKeyboardUtil

/**
 * Created by feiyang on 2017/12/4 12:31
 * Description : 带删除按钮的自定义布局(FrameLayout)
 */
class EditTextWithDelete @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

    private var mInputKeyEt: EditText? = null
    private var mDeleteIv: ImageView? = null
    private var mOnEditTextListener: OnEditTextListener? = null

    /**
     * 设置文本内容
     */
    var text: String = ""
        get() = mInputKeyEt?.text.toString()
        set(value) {
            mInputKeyEt?.setText(value)
            field = value
        }

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_edittext_with_del, this, true)
        mInputKeyEt = findViewById(R.id.et_input_search_key)
        mDeleteIv = findViewById(R.id.iv_input_search_key_del)

        mDeleteIv?.setOnClickListener(this)
        mInputKeyEt?.addTextChangedListener(this)
        mInputKeyEt?.setOnEditorActionListener(this)
    }

    override fun onClick(v: View) {
        val viewId = v.id
        if (viewId == R.id.iv_input_search_key_del) {
            //删除按钮
            mInputKeyEt?.setText("")
        }
    }

    /**
     * 清空输入的值
     */
    fun clearText() {
        mInputKeyEt?.setText("")
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        val inputText = mInputKeyEt?.text.toString()
        if (inputText.isNotEmpty()) {
            mDeleteIv?.visibility = View.VISIBLE
        } else {
            mDeleteIv?.visibility = View.GONE
        }
        mOnEditTextListener?.onTextChange(mInputKeyEt?.text.toString())
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        //当actionId == XX_SEND 或者 XX_DONE时都触发
        //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
        //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
        if (actionId == EditorInfo.IME_ACTION_SEND
                || actionId == EditorInfo.IME_ACTION_DONE
                || event != null && KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent
                .ACTION_DOWN == event.action) {
            //处理事件  回调用户按下enter键时EditText上已输入的值
            if (mOnEditTextListener != null) {
                mOnEditTextListener?.onEnterClick(mInputKeyEt?.text.toString())
            }
            return true
        }
        return false
    }

    /**
     * 设置监听器  用于监听控件的一些事件
     *
     * @param onEditTextListener 监听器
     */
    fun setOnEditTextListener(onEditTextListener: OnEditTextListener) {
        this.mOnEditTextListener = onEditTextListener
    }

    /**
     * 监听用户按下enter键,监听EditText获取焦点
     */
    interface OnEditTextListener {
        /**
         * 回调用户按下enter键时EditText上已输入的值
         *
         * @param key EditText上已输入的值
         */
        fun onEnterClick(key: String)

        /**
         * 输入的文字发生改变
         *
         * @param textContent 输入的内容
         */
        fun onTextChange(textContent: String)

    }

}
