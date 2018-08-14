package com.xfhy.todo.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.view.KeyEvent
import android.view.MenuItem
import com.xfhy.library.basekit.activity.BaseActivity
import com.xfhy.library.common.AppManager
import com.xfhy.todo.R
import com.xfhy.todo.fragment.CompleteFragment
import com.xfhy.todo.fragment.MeFragment
import com.xfhy.todo.fragment.TodoFragment
import com.xfhy.todo.fragment.TomatoFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

/**
 * 2018年8月10日15:42:33
 * @author xfhy
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 */
class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        /**
         * 再按一次退出   中间的时间间隔
         */
        private const val BACK_TO_EXIT_TIME = 3000
    }

    /**
     * 上次点击返回按键时间
     */
    private var lastClickTime = 0L
    private val mTodoFragment: TodoFragment by lazy { TodoFragment.newInstance() }
    private val mCompleteFragment: CompleteFragment by lazy { CompleteFragment.newInstance() }
    private val mTomatoFragment: TomatoFragment by lazy { TomatoFragment.newInstance() }
    private val mMeFragment: MeFragment by lazy { MeFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        bnv_main_bottom_view.setOnNavigationItemSelectedListener(this)

        addFragment(supportFragmentManager, mTodoFragment, "TodoFragment")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        hideAllFragment(supportFragmentManager)
        when (item.itemId) {
            R.id.action_todo -> {
                toast("清单")
                addFragment(supportFragmentManager, mTodoFragment, "TodoFragment")
                showFragment(supportFragmentManager, mTodoFragment)
            }
            R.id.action_complete -> {
                toast("已完成")
                addFragment(supportFragmentManager, mCompleteFragment, "CompleteFragment")
                showFragment(supportFragmentManager, mCompleteFragment)
            }
            R.id.action_tomato -> {
                toast("番茄")
                addFragment(supportFragmentManager, mTomatoFragment, "TomatoFragment")
                showFragment(supportFragmentManager, mTomatoFragment)
            }
            R.id.action_me -> {
                toast("我的")
                addFragment(supportFragmentManager, mMeFragment, "MeFragment")
                showFragment(supportFragmentManager, mMeFragment)
            }
            else -> {
            }
        }
        return true
    }

    private fun hideAllFragment(fragmentManager: FragmentManager) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (!mTodoFragment.isHidden) {
            fragmentTransaction.hide(mTodoFragment)
        }
        if (!mCompleteFragment.isHidden) {
            fragmentTransaction.hide(mCompleteFragment)
        }
        if (!mTomatoFragment.isHidden) {
            fragmentTransaction.hide(mTomatoFragment)
        }
        if (!mMeFragment.isHidden) {
            fragmentTransaction.hide(mMeFragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun addFragment(fragmentManager: FragmentManager, fragment: Fragment, tag: String) {
        if (!fragment.isAdded && fragmentManager.findFragmentByTag(tag) == null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.rl_main_center_view, fragment, tag)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    private fun showFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.show(fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - lastClickTime < BACK_TO_EXIT_TIME) {
                AppManager.instance.exitApp(mContext)
            } else {
                toast("再按一次退出")
                lastClickTime = System.currentTimeMillis()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}

