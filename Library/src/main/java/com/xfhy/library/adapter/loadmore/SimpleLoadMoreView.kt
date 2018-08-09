package com.xfhy.library.adapter.loadmore

import com.xfhy.library.R


/**
 * Created by BlingBling on 2016/10/11.
 */

class SimpleLoadMoreView : LoadMoreView() {

    override val layoutId: Int
        get() = R.layout.quick_view_load_more

    protected override val loadingViewId: Int
        get() = R.id.load_more_loading_view

    protected override val loadFailViewId: Int
        get() = R.id.load_more_load_fail_view

    protected override val loadEndViewId: Int
        get() = R.id.load_more_load_end_view
}
