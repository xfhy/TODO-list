package com.xfhy.library.adapter

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout

import java.lang.annotation.Retention
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.ArrayList

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.xfhy.library.adapter.animation.*
import com.xfhy.library.adapter.loadmore.LoadMoreView
import com.xfhy.library.adapter.loadmore.SimpleLoadMoreView
import java.lang.annotation.RetentionPolicy

/**
 * author xfhy
 * create at 2017/10/16 15:45
 * description：简单的封装BaseAdapter
 */
abstract class BaseQuickAdapter<T, K : BaseViewHolder>
@JvmOverloads constructor(@LayoutRes layoutResId: Int, data: MutableList<T>? = null) : RecyclerView.Adapter<K>() {

    //load more
    /**
     * 可进行下一页加载
     */
    private var mNextLoadEnable = false
    /**
     * 可进行加载更多
     */
    var isLoadMoreEnable = false
        private set
    /**
     * 当前加载状态 是否正在加载
     */
    var isLoading = false
        private set
    /**
     * 加载更多布局  默认是实现了一个简单布局,当然也可以自己实现
     */
    private val mLoadMoreView = SimpleLoadMoreView()
    /**
     * 加载更多回调
     */
    private var mRequestLoadMoreListener: RequestLoadMoreListener? = null
    private val mEnableLoadMoreEndClick = false
    /**
     * 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
     */
    private var mPreLoadNumber = 1

    protected open lateinit var mContext: Context
    /**
     * 数据集合
     */
    protected var mData: MutableList<T>? = null
    /**
     * RecyclerView中普通item的布局id
     */
    protected var mLayoutResId: Int = 0
    protected lateinit var mLayoutInflater: LayoutInflater

    /**
     * 子项item点击事件
     */
    var onItemClickListener: OnItemClickListener<T, K>? = null

    /**
     * 设置item长按事件
     *
     * @param onItemLongClickListener OnItemLongClickListener
     */
    var onItemLongClickListener: OnItemLongClickListener<T, K>? = null

    var headerLayout: LinearLayout? = null
        private set
    var footerLayout: LinearLayout? = null
        private set

    /**
     * 设置header不跨区域  就像正常item
     * true:不跨区域(不占满一行)   false:跨区域(占满一行)
     */
    var isHeaderViewAsFlow: Boolean = false
    /**
     * 设置footer不跨区域  就像正常item
     * true:不跨区域   false:跨区域
     */
    var isFooterViewAsFlow: Boolean = false
    private var mSpanSizeLookup: SpanSizeLookup? = null

    //empty
    /**
     * 用来存放EmptyView的容器
     */
    private var mEmptyLayout: FrameLayout? = null
    /**
     * 是否使用EmptyView
     */
    private var mIsUseEmpty = true
    /**
     * 头布局和空布局共存?
     */
    private var mHeadAndEmptyEnable: Boolean = false
    /**
     * 尾布局和空布局共存
     */
    private var mFootAndEmptyEnable: Boolean = false

    /**
     * 动画只执行1次?
     */
    private var mFirstOnlyEnable = true
    /**
     * 开启了动画?
     */
    private var mOpenAnimationEnable = false
    private val mInterpolator = LinearInterpolator()
    /**
     * 动画播放时长
     */
    private var mDuration = 300
    /**
     * 上一个在播放动画的item的位置
     */
    private var mLastPosition = -1

    /**
     * 自定义的动画
     */
    private var mCustomAnimation: BaseAnimation? = null
    /**
     * 当前选择使用哪种动画,这里默认是渐显
     */
    private var mSelectAnimation: BaseAnimation = AlphaInAnimation()

    protected var mRecyclerView: RecyclerView? = null
        private set

    /*--------------------------header-footer----------------------------------*/

    /**
     * if addHeaderView will be return 1, if not will be return 0
     * 添加了header则返回1   没有header则返回0
     */
    val headerLayoutCount: Int
        get() = if (headerLayout == null || headerLayout?.childCount == 0) {
            0
        } else 1

    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    private val footerLayoutCount: Int
        get() = if (footerLayout == null || footerLayout?.childCount == 0) {
            0
        } else 1

    /*-------------------data操作-----------------------------*/

    /**
     * Get the data of list
     *
     * @return 列表数据
     */
    val data: MutableList<T>?
        get() = mData

    /**
     * 判断是否可以进行加载更多的逻辑
     *
     * @return 0 or 1
     */
    //参数合法性    加载更多状态
    //可加载下一页               有无更多数据
    //当前数据项个数
    private val loadMoreViewCount: Int
        get() {
            if (mRequestLoadMoreListener == null || !isLoadMoreEnable) {
                return 0
            }
            if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone) {
                return 0
            }
            return if (mData?.size == 0) {
                0
            } else 1
        }

    /**
     * 获取加载更多的布局的索引
     *
     * @return
     */
    private val loadMoreViewPosition: Int
        get() = headerLayoutCount + (mData?.size ?: 0) + footerLayoutCount

    val emptyView: View?
        get() = mEmptyLayout

    /**
     * 返回HeaderView在RecyclerView中的位置
     *有空布局 并且 头布局可见
     * 没有空布局   返回0
     * @return 0 or -1
     */
    private val headerViewPosition: Int
        get() {
            if (emptyViewCount == 1) {
                if (mHeadAndEmptyEnable) {
                    return 0
                }
            } else {
                return 0
            }
            return -1
        }

    //空布局可见 并且 头布局可见
    //尾布局可见
    //头布局有无:0 or 1              正常项的大小
    private val footerViewPosition: Int
        get() {
            if (emptyViewCount == 1) {
                var position = 1
                if (mHeadAndEmptyEnable && headerLayoutCount != 0) {
                    position++
                }
                if (mFootAndEmptyEnable) {
                    return position
                }
            } else {
                return headerLayoutCount + (mData?.size ?: 0)
            }
            return -1
        }

    private val emptyViewCount: Int
        get() {
            if (mEmptyLayout == null || mEmptyLayout?.childCount == 0) {
                return 0
            }
            if (!mIsUseEmpty) {
                return 0
            }
            return if (mData?.size != 0) {
                0
            } else 1
        }

    @IntDef(ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT, SLIDEIN_TOP)
    @Retention(RetentionPolicy.SOURCE)
    annotation class AnimationType

    /**
     * 方向
     */
    @IntDef(LinearLayout.HORIZONTAL, LinearLayout.VERTICAL)
    @Retention(RetentionPolicy.SOURCE)
    annotation class OrientationMode

    private fun checkNotNull() {
        if (mRecyclerView == null) {
            throw RuntimeException("please bind recyclerView first!")
        }
    }

    init {
        this.mData = data ?: ArrayList()
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId
        }
    }

    constructor(data: MutableList<T>?) : this(0, data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {
        val baseViewHolder: K?
        this.mContext = parent.context
        this.mLayoutInflater = LayoutInflater.from(mContext)
        when (viewType) {
            LOADING_VIEW -> baseViewHolder = getLoadingView(parent)
            HEADER_VIEW -> baseViewHolder = createBaseViewHolder(headerLayout as View)
            EMPTY_VIEW -> baseViewHolder = createBaseViewHolder(mEmptyLayout as View)
            FOOTER_VIEW -> baseViewHolder = createBaseViewHolder(footerLayout as View)
            else -> {
                baseViewHolder = onCreateDefViewHolder(parent, viewType)
                bindViewClickListener(baseViewHolder)
            }
        }
        return baseViewHolder
    }

    private fun getLoadingView(parent: ViewGroup): K {
        //加载 加载布局
        val view = getItemView(mLoadMoreView.layoutId, parent)
        //生成baseviewholder
        val holder = createBaseViewHolder(view)
        //设置加载布局的点击事件
        holder.itemView.setOnClickListener {
            if (mLoadMoreView.loadMoreStatus == LoadMoreView.STATUS_FAIL) {
                //之前是加载失败状态时   前去刷新
                notifyLoadMoreToLoading()
            }
            if (mEnableLoadMoreEndClick && mLoadMoreView.loadMoreStatus == LoadMoreView.STATUS_END) {
                //加载更多布局可以被点击  并且  之前状态是结束状态
                notifyLoadMoreToLoading()
            }
        }
        return holder
    }

    /**
     * 通知启动回调并加载更多
     */
    fun notifyLoadMoreToLoading() {
        //如果当前正在加载中,则不用管
        if (mLoadMoreView.loadMoreStatus == LoadMoreView.STATUS_LOADING) {
            return
        }
        //将加载更多布局的状态设置为默认状态  这样当下面刷新adapter时会回调onBindViewHolder()从而触发
        //autoLoadMore()方法去判断是否需要加载更多,这时候刚好又是默认状态是可以更新的,于是就去回调onLoadMoreRequested()方法
        mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * 创建默认的ViewHolder  即中间的数据项的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected open fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
        val layoutId = mLayoutResId
        /*if (mMultiTypeDelegate != null) {  //不同的类型的布局
            layoutId = mMultiTypeDelegate.getLayoutId(viewType);
        }*/
        return createBaseViewHolder(parent, mLayoutResId)
    }

    protected fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): K {
        return createBaseViewHolder(getItemView(layoutResId, parent))
    }

    protected fun getItemView(@LayoutRes layoutResId: Int, parent: ViewGroup): View {
        return mLayoutInflater.inflate(layoutResId, parent, false)
    }

    /**
     * 如果要在适配器中使用BaseViewHolder的子类，
     *       *您必须覆盖该方法才能创建新的ViewHolder。
     *
     * @param view view
     * @return new ViewHolder
     */
    protected fun createBaseViewHolder(view: View): K {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            //判断z是否是BaseViewHolder的子类或接口  不是则返回null
            z = getInstancedGenericKClass(temp)
            //返回超类
            temp = temp.superclass
        }
        val k: K?
        // 泛型擦除会导致z为null
        k = if (z == null) {
            //为null则说明z不是BaseViewHolder的子类或接口 则创建一个BaseViewHolder
            BaseViewHolder(view) as K
        } else {
            //尝试创建z的实例   利用反射
            createGenericKInstance(z, view)
        }
        return k ?: BaseViewHolder(view) as K
    }

    /**
     * get generic parameter K
     * 判断z是否是BaseViewHolder的子类或接口
     *
     * @param z
     * @return
     */
    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        //getGenericSuperclass()获得带有泛型的父类
        //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
        val type = z.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (temp in types) {
                //判断tempClass是否是BaseViewHolder类型相同或具有相同的接口
                if (temp is Class<*>) {
                    if (BaseViewHolder::class.java.isAssignableFrom(temp)) {
                        return temp
                    }
                } else if (temp is ParameterizedType) {
                    val rawType = temp.rawType
                    if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(
                                    rawType)) {
                        return rawType
                    }
                }
            }
        }
        return null
    }

    /**
     * 尝试创建Generic K实例
     *
     * @param z
     * @param view
     * @return
     */
    private fun createGenericKInstance(z: Class<*>, view: View?): K? {
        try {
            val constructor: Constructor<*>
            // inner and unstatic class
            //成员类&&非静态类
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                //获取z的构造函数
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                //禁止java语言访问检查
                constructor.isAccessible = true
                //通过构造方法构造z对象
                constructor.newInstance(this, view) as K
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(view) as K
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 绑定item的点击事件
     */
    private fun bindViewClickListener(baseViewHolder: BaseViewHolder?) {
        if (baseViewHolder == null) {
            return
        }
        val view = baseViewHolder.itemView ?: return
        if (onItemClickListener != null) {
            view.setOnClickListener { v ->
                onItemClickListener?.onItemClick(this@BaseQuickAdapter, v, baseViewHolder
                        .layoutPosition - headerLayoutCount)
            }
        }
        if (onItemLongClickListener != null) {
            view.setOnLongClickListener { v ->
                onItemLongClickListener?.onItemLongClick(this@BaseQuickAdapter, v,
                        baseViewHolder.layoutPosition - headerLayoutCount) ?: false
            }
        }
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        //判断是否需要进行上拉加载更多
        autoLoadMore(position)

        val viewType = holder.itemViewType
        when (viewType) {
            0 -> convert(holder, getItem(position - headerLayoutCount))
            LOADING_VIEW -> mLoadMoreView.convert(holder)
            HEADER_VIEW -> {
            }
            EMPTY_VIEW -> {
            }
            FOOTER_VIEW -> {
            }
            else -> convert(holder, getItem(position - headerLayoutCount))
        }
    }

    override fun getItemCount(): Int {
        var count: Int
        //如果界面上显示了空布局
        if (emptyViewCount == 1) {
            count = 1
            //如果header可见  则只需要+1就行了,因为header的布局是LinearLayout
            if (mHeadAndEmptyEnable && headerLayoutCount != 0) {
                count++
            }
            if (mFootAndEmptyEnable && footerLayoutCount != 0) {
                count++
            }
        } else {  //未显示空布局
            //         1 or 0                  数据项             1 or 0                 加载更多 1 or 0
            count = headerLayoutCount + (mData?.size ?: 0) + footerLayoutCount +
                    loadMoreViewCount
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        /*
        这里有空布局的逻辑一起混起的,干脆一起分析,反正不是很难.

        - 当有空布局的时候,position的值只可能为0,1,2;
        再根据是否显示了header,即可判断出当前position的type应该是什么.
        - 再看没有显示空布局的情况
            - header类型  索引:`< 1`
            - 中间的数据项类型  需要减去header的数量(1 or 0) ;这里面牵涉到了item多种类型的逻辑,
            如果是多种类型的则交由mMultiTypeDelegate去处理,如果不是,则默认返回super.getItemViewType(position);
            - footer类型  减去header的数量(1 or 0),再减去中间数据项的数量
            - 加载中类型 剩下的
         */
        if (emptyViewCount == 1) {
            val header = mHeadAndEmptyEnable && headerLayoutCount != 0
            return when (position) {
                0 -> if (header) {
                    HEADER_VIEW
                } else {
                    EMPTY_VIEW
                }
                1 -> if (header) {
                    EMPTY_VIEW
                } else {
                    FOOTER_VIEW
                }
                2 -> FOOTER_VIEW
                else -> EMPTY_VIEW
            }
        }
        //1 or 0
        val numHeaders = headerLayoutCount
        if (position < numHeaders) {
            return HEADER_VIEW
        } else {
            var adjPosition = position - numHeaders
            val adapterCount = mData?.size ?: 0

            //中间的数据项
            return if (adjPosition < adapterCount) {
                getDefItemViewType(adjPosition)
            } else {
                //剩下 footer   加载中布局
                adjPosition -= adapterCount
                val numFooters = footerLayoutCount
                if (adjPosition < numFooters) {
                    FOOTER_VIEW
                } else {
                    LOADING_VIEW
                }
            }
        }
    }

    protected open fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    /**
     * @param header
     * @param index
     * @param orientation
     */
    @JvmOverloads
    fun addHeaderView(header: View, @IntRange(from = -1) index: Int = -1,
                      @OrientationMode orientation: Int = LinearLayout.VERTICAL): Int {
        var index = index

        // 如果为空 则创建头布局
        if (headerLayout == null) {
            headerLayout = LinearLayout(header.context)
            // 方向  LayoutParams设置
            if (orientation == LinearLayout.VERTICAL) {
                headerLayout?.orientation = LinearLayout.VERTICAL
                headerLayout?.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,
                        WRAP_CONTENT)
            } else {
                headerLayout?.orientation = LinearLayout.HORIZONTAL
                headerLayout?.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT,
                        MATCH_PARENT)
            }
        }


        val childCount = headerLayout?.childCount ?: 0
        // 如果index不合法,则添加到索引最大的位置
        if (index < 0 || index > childCount) {
            index = childCount
        }
        headerLayout?.addView(header, index)   //就是添加到LinearLayout中

        /*
        如果头布局(LinearLayout)中子View(header的item)的数量等于1
         说明这是第一次添加headerLayout进RecyclerView
         需要进行通知刷新操作  告知RecyclerView第一个索引处更新啦
         */
        if (headerLayout?.childCount == 1) {
            val position = headerViewPosition
            if (position != -1) {
                notifyItemInserted(position) //告知RecyclerView第一个索引处更新啦
                // 这时RecyclerView中的第一项已经是headerLayout(LinearLayout)了
            }
        }
        return index
    }

    @JvmOverloads
    fun setHeaderView(header: View, @IntRange(from = -1) index: Int = 0,
                      @OrientationMode orientation: Int = LinearLayout.VERTICAL): Int {
        return if (headerLayout == null || (headerLayout?.childCount ?: 0) <= index) {
            addHeaderView(header, index, orientation)
        } else {
            headerLayout?.removeViewAt(index)
            headerLayout?.addView(header, index)
            index
        }
    }

    @JvmOverloads
    fun addFooterView(footer: View, @IntRange(from = -1) index: Int = -1,
                      @OrientationMode orientation: Int = LinearLayout.VERTICAL): Int {
        var index = index
        if (footerLayout == null) {
            footerLayout = LinearLayout(footer.context)
            if (orientation == LinearLayout.VERTICAL) {
                footerLayout?.orientation = LinearLayout.VERTICAL
                footerLayout?.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,
                        WRAP_CONTENT)
            } else {
                footerLayout?.orientation = LinearLayout.HORIZONTAL
                footerLayout?.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT,
                        MATCH_PARENT)
            }
        }
        val childCount = footerLayout?.childCount ?: 0
        if (index < 0 || index > childCount) {
            index = childCount
        }
        footerLayout?.addView(footer, index)
        if (footerLayout?.childCount == 1) {
            val position = footerViewPosition
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }

    @JvmOverloads
    fun setFooterView(header: View, @IntRange(from = -1) index: Int = 0,
                      @OrientationMode orientation: Int = LinearLayout.VERTICAL): Int {
        return if (footerLayout == null || (footerLayout?.childCount ?: 0) <= index) {
            addFooterView(header, index, orientation)
        } else {
            footerLayout?.removeViewAt(index)
            footerLayout?.addView(header, index)
            index
        }
    }

    fun removeHeaderView(header: View) {
        if (headerLayoutCount == 0) return

        headerLayout?.removeView(header)
        //如果mHeaderLayout已经没有子View,则直接将mHeaderLayout从RecyclerView中移除
        if (headerLayout?.childCount == 0) {
            val position = headerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeFooterView(footer: View) {
        if (footerLayoutCount == 0) return

        footerLayout?.removeView(footer)
        //如果mFooterLayout已经没有子View,则直接将mHeaderLayout从RecyclerView中移除
        if (footerLayout?.childCount == 0) {
            val position = footerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    /**
     * 移除所有header view
     */
    fun removeAllHeaderView() {
        if (headerLayoutCount == 0) return

        headerLayout?.removeAllViews()
        val position = headerViewPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    /**
     * 移除所有footer view
     */
    fun removeAllFooterView() {
        if (footerLayoutCount == 0) return

        footerLayout?.removeAllViews()
        val position = footerViewPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    /**
     * 判断当前type是否是特殊的type
     */
    protected open fun isFixedViewType(type: Int): Boolean {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW
    }

    fun getViewByPosition(position: Int, @IdRes viewId: Int): View? {
        checkNotNull()
        return getViewByPosition(mRecyclerView, position, viewId)
    }

    fun getViewByPosition(recyclerView: RecyclerView?, position: Int, @IdRes viewId: Int): View? {
        if (recyclerView == null) {
            return null
        }
        val viewHolder = recyclerView.findViewHolderForLayoutPosition(position) as BaseViewHolder
        return viewHolder.getView(viewId)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * RecyclerView在开始观察该适配器时调用。
     * 请记住，多个RecyclerView可能会观察到相同的适配器。
     *
     *
     * Adapter与RecyclerView关联起来
     * 这里面主要是做表格布局管理器的头布局和脚布局自占一行的适配
     *
     * @param recyclerView
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView?.layoutManager
        if (manager is GridLayoutManager) {
//设置adapter中每个Item所占用的跨度数
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)

                    //如果当前type为header并且asFlow为true:那么header设置为不跨区域,就和正常item一样.只占1格
                    if (type == HEADER_VIEW && isHeaderViewAsFlow) {
                        return 1
                    }
                    if (type == FOOTER_VIEW && isFooterViewAsFlow) {
                        return 1
                    }

                    //如果用户没有自定义SpanSizeLookup  SpanSizeLookup是用来查询每个item占用的跨度数的实例
                    return if (mSpanSizeLookup == null) {
                        /*
                        1.如果是特殊的type,那么item的跨度设置为当前gridManager的SpanCount
                        即如果我设置的是mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        那么当前item占2格的跨度
                        2.如果item不是特殊的item,那么就是占1个格子,不跨
                        */
                        if (isFixedViewType(type)) manager.spanCount else 1
                    } else {
                        /*
                        1.如果是特殊的type,那么item的跨度设置为当前gridManager的SpanCount
                        即如果我设置的是mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        那么当前item占2格的跨度
                        2.如果item不是特殊的item,那么交给外部调用者来处理每个item应该占多少个格
                        */
                        if (isFixedViewType(type))
                            manager.spanCount
                        else
                            mSpanSizeLookup?.getSpanSize(manager,
                                    position - headerLayoutCount) ?: 1
                    }
                }


            }
        }
    }

    /**
     * 用于外部调用者设置每个item的跨度,除了header,footer,emptyView,loadMoreView
     */
    interface SpanSizeLookup {
        fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     * 用于查询每个item占用的跨度数的实例
     */
    fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup
    }

    fun getItem(@IntRange(from = 0) position: Int): T? {
        return if (position < (mData?.size ?: 0))
            mData?.get(position)
        else null
    }

    /**
     * 绑定数据
     *
     * @param holder BaseViewHolder
     * @param item   item数据
     */
    protected abstract fun convert(holder: BaseViewHolder, item: T?)

    /**
     * 删除item
     *
     * @param position 删除item的位置
     */
    fun removeItem(@IntRange(from = 0) position: Int) {
        if (mData == null || position < 0) {
            return
        }
        if (position >= (mData?.size ?: 0)) {
            return
        }

        mData?.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * 新增item
     *
     * @param position 新增item位置
     * @param item     item数据
     */
    fun addItem(@IntRange(from = 0) position: Int, item: T) {
        if (mData == null) {
            return
        }
        if (position > (mData?.size ?: 0)) {
            return
        }
        mData?.add(position, item)
        notifyItemInserted(position)
    }

    /**
     * 添加一个集合的数据
     */
    fun addData(@IntRange(from = 0) position: Int, newData: MutableList<T>) {
        mData?.addAll(position, newData)
        notifyItemRangeInserted(position + headerLayoutCount, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * 添加数据到末尾
     */
    fun addData(newData: MutableList<T>?) {
        if (newData == null) {
            return
        }
        mData?.addAll(newData)
        notifyItemRangeInserted((mData?.size ?: 0) - newData.size + headerLayoutCount, newData
                .size)
        compatibilityDataSizeChanged(newData.size)
    }

    fun replaceData(data: MutableList<T>?) {
        // 不是同一个引用才清空列表
        if (data != null && data !== mData) {
            mData?.clear()
            mData?.addAll(data)
        }
        notifyDataSetChanged()
    }

    /**
     * 清空数据
     */
    fun clearData() {
        if (mData != null) {
            mData?.clear()
        }
        notifyDataSetChanged()
    }

    /**
     * 判断loadMoreView和emptyView是否已经变化
     */
    private fun compatibilityDataSizeChanged(size: Int) {
        val dataSize = if (mData == null) 0 else mData?.size
        if (dataSize == size) {
            notifyDataSetChanged()
        }
    }

    /**
     * 设置新数据
     */
    fun setNewData(data: MutableList<T>?) {
        this.mData = data ?: ArrayList()

        //如果当前设置了加载更多监听器
        if (mRequestLoadMoreListener != null) {
            // 状态切换为可加载下一页
            mNextLoadEnable = true
            isLoadMoreEnable = true
            isLoading = false
            mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
        }
        mLastPosition = -1
        notifyDataSetChanged()
    }

    /*---------------------动画-----------------------*/

    /**
     * 设置count个不执行动画
     */
    fun setNotDoAnimationCount(@IntRange(from = 0) count: Int) {
        mLastPosition = count
    }

    override fun onViewAttachedToWindow(holder: K) {
        super.onViewAttachedToWindow(holder)
        val type = holder?.itemViewType
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) {
            //设置为跨区域  比如是StaggeredGridLayoutManager时,header或者footer等应该如何展示
            setFullSpan(holder)
        } else {
            //添加动画到holder的itemView上,并执行动画
            addAnimation(holder)
        }
    }

    protected fun setFullSpan(holder: RecyclerView.ViewHolder) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val params = holder
                    .itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            params.isFullSpan = true
        }
    }

    /**
     * 添加动画到item上并执行动画
     *
     * @param holder
     */
    private fun addAnimation(holder: RecyclerView.ViewHolder?) {
        // 判断是否开启了动画
        if (mOpenAnimationEnable) {
            //  !isFirstOnly            这一次item的位置>上一次加载动画item的位置
            if (!mFirstOnlyEnable || (holder?.layoutPosition ?: 0) > mLastPosition) {
                var animation: BaseAnimation? = null
                //判断是否是自定义了动画
                animation = if (mCustomAnimation != null) {
                    mCustomAnimation
                } else {
                    //没有自定义,则使用默认的动画
                    mSelectAnimation
                }
                // 遍历定义到holder.itemView上的动画  BaseAnimation使用的getAnimators()是获取动画数组,方便扩展
                // 如果用户需要自定义动画的话,则可以在一个item上同时加入多个动画,然后下面让这些动画依次执行
                animation?.getAnimators(holder?.itemView)?.forEach {
                    //开启动画
                    startAnim(it, (holder?.layoutPosition ?: 0))
                }

                //记录这一次执行动画的item
                mLastPosition = (holder?.layoutPosition ?: 0)
            }
        }
    }

    /**
     * 开启动画并设置插值器
     *
     * @param anim
     * @param index
     */
    protected fun startAnim(anim: Animator, index: Int) {
        anim.setDuration(mDuration.toLong()).start()
        anim.interpolator = mInterpolator
    }

    /**
     * Set the view animation type.
     * 设置动画类型
     *
     * @param animationType One of [.ALPHAIN], [.SCALEIN], [.SLIDEIN_BOTTOM],
     * [.SLIDEIN_LEFT], [.SLIDEIN_RIGHT].
     */
    @SuppressLint("SwitchIntDef")
    fun openLoadAnimation(@AnimationType animationType: Int) {
        //标志着需要加载动画
        this.mOpenAnimationEnable = true
        //用户没有自定义动画  是使用的默认动画
        mCustomAnimation = null
        //根据用户传入的类型初始化应该使用哪种动画
        mSelectAnimation = when (animationType) {
            ALPHAIN -> AlphaInAnimation()
            SCALEIN -> ScaleInAnimation()
            SLIDEIN_BOTTOM -> SlideInBottomAnimation()
            SLIDEIN_LEFT -> SlideInLeftAnimation()
            SLIDEIN_RIGHT -> SlideInRightAnimation()
            SLIDEIN_TOP -> SlideInTopAnimation()
            else -> {
                AlphaInAnimation()
            }
        }
    }

    fun setDuration(@IntRange(from = 0) duration: Int) {
        mDuration = duration
    }

    /**
     * 自定义动画
     */
    fun openLoadAnimation(animation: BaseAnimation) {
        //标志着需要加载动画
        this.mOpenAnimationEnable = true
        //初始化自定义动画
        this.mCustomAnimation = animation
    }

    /**
     * 开启动画,这种情况下会默认开启:渐显动画
     */
    fun openLoadAnimation() {
        this.mOpenAnimationEnable = true
    }

    /**
     * 设置动画是否只加载一次
     *
     * every time  true:第一次显示时才加载动画   false:每次都加载动画
     */
    fun isFirstOnly(firstOnly: Boolean) {
        this.mFirstOnlyEnable = firstOnly
    }

    /*---------------------------------上拉加载更多--------------------------------------*/

    /**
     * 设置当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested()方法
     *
     * @param preLoadNumber
     */
    fun setPreLoadNumber(@IntRange(from = 1) preLoadNumber: Int) {
        mPreLoadNumber = preLoadNumber
    }

    /**
     * 根据position位置判断当前是否需要进行加载更多
     *
     * @param position 当前onBindViewHolder()的Position
     */
    private fun autoLoadMore(position: Int) {
        // 判断是否可以进行加载更多的逻辑
        if (loadMoreViewCount == 0) {
            return
        }
        //只有在当前列表的倒数mPreLoadNumber个item开始绑定数据时才进行加载更多的逻辑
        if (position < itemCount - mPreLoadNumber) {
            return
        }
        //判断当前加载状态,如果不是默认状态(可能正处于 正在加载中 的状态),则不进行加载
        if (mLoadMoreView.loadMoreStatus != LoadMoreView.STATUS_DEFAULT) {
            return
        }
        //设置当前状态:加载中
        mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_LOADING
        if (!isLoading) {
            isLoading = true
            if (mRecyclerView != null) {
                mRecyclerView?.post {
                    //回调  让调用者去处理加载更多的逻辑
                    mRequestLoadMoreListener?.onLoadMoreRequested()
                }
            } else {
                mRequestLoadMoreListener?.onLoadMoreRequested()
            }
        }
    }

    fun bindToRecyclerView(recyclerView: RecyclerView) {
        if (recyclerView != null) {
            throw RuntimeException("Don't bind twice")
        }
        mRecyclerView = recyclerView
        mRecyclerView?.adapter = this
    }

    fun disableLoadMoreIfNotFullPage() {
        //检查当前RecyclerView是否为null
        checkNotNull()
        disableLoadMoreIfNotFullPage(mRecyclerView)
    }

    /**
     * 不是配置项！！
     *
     *
     * 这个方法是用来检查是否满一屏的，所以只推荐在 [.setNewData] 之后使用
     * 原理:先关闭 load more，检查完了再决定是否开启
     * 数据项个数未满一屏幕,则不开启load more
     * 数据项个数 > 一屏幕,则继续开启load more
     *
     *
     * 不是配置项！！
     *
     * @param recyclerView your recyclerView
     * @see .setNewData
     */
    fun disableLoadMoreIfNotFullPage(recyclerView: RecyclerView?) {
        // 设置加载状态为false
        setEnableLoadMore(false)
        if (recyclerView == null) return
        val manager = recyclerView.layoutManager ?: return
        if (manager is LinearLayoutManager) {
            recyclerView.postDelayed({
                //数据项个数 > 一屏幕,则继续开启load more
                if (manager.findLastCompletelyVisibleItemPosition() + 1 != itemCount) {
                    setEnableLoadMore(true)
                }
            }, 50)
        } else if (manager is StaggeredGridLayoutManager) {
            recyclerView.postDelayed({
                //返回StaggeredGridLayoutManager布局的跨度数
                val positions = IntArray(manager.spanCount)
                //返回每一个跨度(列)的最后一个可见的item的位置  赋值到该数组里面
                manager.findLastCompletelyVisibleItemPositions(positions)
                //找出数组中最大的数(即StaggeredGridLayoutManager布局的当前可见的最下面那个item)
                val pos = getTheBiggestNumber(positions) + 1
                // 数据项个数 > 一屏幕,则继续开启load more
                if (pos != itemCount) {
                    setEnableLoadMore(true)
                }
            }, 50)
        }
    }

    /**
     * 设置上拉加载更多是否可用
     */
    fun setEnableLoadMore(enable: Boolean) {
        //之前的状态需要和现在的状态做对比
        val oldLoadMoreCount = loadMoreViewCount
        isLoadMoreEnable = enable
        val newLoadMoreCount = loadMoreViewCount

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                //之前有 现在没有 需要移除
                notifyItemRemoved(loadMoreViewPosition)
            }
        } else {
            if (newLoadMoreCount == 1) {
                //将加载布局插入
                mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
                notifyItemInserted(loadMoreViewPosition)
            }
        }
    }

    /**
     * 返回数组中的最大值
     */
    private fun getTheBiggestNumber(numbers: IntArray?): Int {
        var tmp = -1
        if (numbers == null || numbers.isEmpty()) {
            return tmp
        }
        for (num in numbers) {
            if (num > tmp) {
                tmp = num
            }
        }
        return tmp
    }

    /**
     * 刷新完成时调用
     */
    fun loadMoreComplete() {
        if (loadMoreViewCount == 0) {
            return
        }
        //将当前加载状态改为false  表示未在加载
        isLoading = false
        //可进行下一页加载
        mNextLoadEnable = true
        // 恢复加载更多布局的状态
        mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
        // 告知加载更多布局被更新了,需要刷新一下
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * 加载失败
     */
    fun loadMoreFail() {
        if (loadMoreViewCount == 0) {
            return
        }
        //当前加载状态  切换为未在加载中
        isLoading = false
        //加载布局设置为加载失败
        mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_FAIL
        //通知加载布局更新了,需要刷新
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * 加载更多,并且没有更多数据了   调用此方法即表示无更多数据了
     * gone:设置加载更多布局是否可见   true:不可见   false:可见
     */
    @JvmOverloads
    fun loadMoreEnd(gone: Boolean = false) {
        if (loadMoreViewCount == 0) {
            return
        }
        ////当前加载状态  切换为未在加载中
        isLoading = false
        //不能再加载下一页了  因为已经没有更多数据了
        mNextLoadEnable = false
        //设置加载更多布局是否可见
        mLoadMoreView.setLoadMoreEndGone(gone)
        if (gone) {
            //如果布局不可见,则更新
            notifyItemRemoved(loadMoreViewPosition)
        } else {
            //如果布局可见,则先更新布局(切换为STATUS_END状态那种布局)
            mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_END
            //并更新adapter
            notifyItemChanged(loadMoreViewPosition)
        }
    }

    /**
     * 设置监听RecyclerView上拉加载更多  并设置监听器
     */
    fun setOnLoadMoreListener(requestLoadMoreListener: RequestLoadMoreListener,
                              recyclerView: RecyclerView) {
        openLoadMore(requestLoadMoreListener)
        mRecyclerView = recyclerView
    }

    /**
     * 开启上拉加载更多
     */
    private fun openLoadMore(requestLoadMoreListener: RequestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener
        mNextLoadEnable = true
        isLoadMoreEnable = true
        isLoading = false
    }

    /*-------------------emptyView-----------------------*/
    fun setEmptyView(@LayoutRes layoutResId: Int, viewGroup: ViewGroup?) {
        val view = LayoutInflater.from(viewGroup?.context).inflate(layoutResId, viewGroup,
                false)
        setEmptyView(view)
    }

    fun setEmptyView(@LayoutRes layoutResId: Int) {
        checkNotNull()
        setEmptyView(layoutResId, mRecyclerView)
    }

    fun setEmptyView(emptyView: View) {
        var insert = false
        if (mEmptyLayout == null) {
            mEmptyLayout = FrameLayout(emptyView.context)

            // 默认空布局是match_parent
            val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT)
            val lp = emptyView.layoutParams
            if (lp != null) {
                layoutParams.width = lp.width
                layoutParams.height = lp.height
            }
            mEmptyLayout?.layoutParams = layoutParams
            insert = true
        }

        //将需要显示的View添加到mEmptyLayout(FrameLayout)中
        mEmptyLayout?.removeAllViews()
        mEmptyLayout?.addView(emptyView)
        mIsUseEmpty = true
        if (insert) { // 如果是第一次初始化空布局,那么需要通知adapter刷新
            if (emptyViewCount == 1) {
                var position = 0

                //判断有无header,如果有的话,那么header无需刷新 只需要刷新空布局即可
                if (mHeadAndEmptyEnable && headerLayoutCount != 0) {
                    position++
                }
                notifyItemInserted(position)
            }
        }
    }

    /**
     * 设置当数据为空时是否显示headerView
     * 调用此方法前需要调用[RecyclerView.setAdapter]
     *
     * @param isHeadAndEmpty false:当数据为空时不显示headerView,只显示EmptyView  true:
     * 当数据为空时显示emptyView和headerView
     */
    fun setHeaderAndEmpty(isHeadAndEmpty: Boolean) {
        setHeaderFooterEmpty(isHeadAndEmpty, false)
    }

    /**
     * 设置数据为空时是否显示headerView和footerView
     * 调用此方法前需要调用 [RecyclerView.setAdapter]
     *
     * @param isHeadAndEmpty true:显示headerView false:不显示headerView
     * @param isFootAndEmpty true:显示footerVIew false:不显示footerVIew
     */
    fun setHeaderFooterEmpty(isHeadAndEmpty: Boolean, isFootAndEmpty: Boolean) {
        mHeadAndEmptyEnable = isHeadAndEmpty
        mFootAndEmptyEnable = isFootAndEmpty
    }

    /**
     * Set whether to use empty view
     * 设置是否需要使用EmptyView
     *
     * @param isUseEmpty true:使用   false:不使用
     */
    fun isUseEmpty(isUseEmpty: Boolean) {
        mIsUseEmpty = isUseEmpty
    }

    /**
     * item点击监听器
     */
    interface OnItemClickListener<T, K : BaseViewHolder> {
        /**
         * item点击事件回调
         *
         * @param view     触发事件View
         * @param position 触发事件的view所在RecyclerView中的位置
         */
        fun onItemClick(adapter: BaseQuickAdapter<T, K>, view: View, position: Int)
    }

    /**
     * item长按监听器
     */
    interface OnItemLongClickListener<T, K : BaseViewHolder> {
        /**
         * item长按事件回调
         *
         * @param view     触发事件View
         * @param position 触发事件的view所在RecyclerView中的位置
         * @return 是否消费
         */
        fun onItemLongClick(adapter: BaseQuickAdapter<T, K>, view: View, position: Int): Boolean
    }

    /**
     * 加载更多监听器
     */
    interface RequestLoadMoreListener {
        /**
         * 当需要加载更多时会被调用
         */
        fun onLoadMoreRequested()
    }

    companion object {

        //item类型
        const val HEADER_VIEW = 0x00000111
        const val LOADING_VIEW = 0x00000222
        const val FOOTER_VIEW = 0x00000333
        const val EMPTY_VIEW = 0x00000555

        //Animation
        const val ALPHAIN = 0x00000001
        const val SCALEIN = 0x00000002
        const val SLIDEIN_BOTTOM = 0x00000003
        const val SLIDEIN_LEFT = 0x00000004
        const val SLIDEIN_RIGHT = 0x00000005
        const val SLIDEIN_TOP = 0x00000006
    }

}

