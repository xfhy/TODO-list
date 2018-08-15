package com.xfhy.todo.activity

import android.animation.Animator
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.xfhy.library.basekit.activity.BaseActivity
import com.xfhy.library.utils.SPUtils
import com.xfhy.todo.R
import com.xfhy.todo.common.Constant
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

/**
 * 2018年8月14日17:33:40
 * @author xfhy
 * 闪屏页
 */
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)// 隐藏标题
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        mSplashAnimationView.addAnimatorListener(object:Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if(SPUtils.getValue(Constant.IS_LOGIN,false)){
                    startActivity<MainActivity>()
                } else {
                    startActivity<LoginActivity>()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
}
