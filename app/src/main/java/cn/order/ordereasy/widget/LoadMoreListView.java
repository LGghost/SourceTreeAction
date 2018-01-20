package cn.order.ordereasy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import cn.order.ordereasy.R;
import cn.order.ordereasy.view.activity.AllTradeActivity;


public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private Context mContext;
    private View mFootView;
    private boolean isScrollToBottom; // 是否滑动到底部
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mIsLoading = false;
    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private int footerViewHeight; // 脚布局的高度

    public LoadMoreListView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mFootView = LayoutInflater.from(context).inflate(R.layout.foot_view, null);
        mFootView.measure(0, 0);
        footerViewHeight = mFootView.getMeasuredHeight();
        mFootView.setPadding(0, -footerViewHeight, 0, 0);
        setOnScrollListener(this);
        this.addFooterView(mFootView);
    }


    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {
            // 判断当前是否已经到了底部
            if (isScrollToBottom && !mIsLoading) {
                mIsLoading = true;
                // 当前到底部
                mFootView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());

                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onloadMore();
                }
            }
        }

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;

        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }

    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
        mFootView.setPadding(0, -footerViewHeight, 0, 0);
    }

    public interface OnLoadMoreListener {
        void onloadMore();
    }

    public void setLoadCompleted() {
        mIsLoading = false;
        mFootView.setPadding(0, -footerViewHeight, 0, 0);

    }
}

