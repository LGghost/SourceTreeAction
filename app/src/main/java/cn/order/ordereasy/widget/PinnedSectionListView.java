package cn.order.ordereasy.widget;


import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * 列表视图,能够销部分视图在顶部,其余的则仍在滚动。
 */
public class PinnedSectionListView extends ListView {

    //-- inner classes

    /**
     * 列表适配器实现的用于PinnedSectionListView适配器.
     */
    public interface PinnedSectionListAdapter extends ListAdapter {
        /**
         * 这个方法将返回“true”如果给定类型的观点必须是固定的.
         */
        boolean isItemViewTypePinned(int viewType);
    }

    /**
     * 包装类固定部分视图和它的位置在列表中。
     */
    static class PinnedSection {
        public View view;
        public int position;
        public long id;
    }

    //-- class fields

    // 字段用于处理触摸事件
    private final Rect mTouchRect = new Rect();
    private final PointF mTouchPoint = new PointF();
    private int mTouchSlop;
    private View mTouchTarget;
    private MotionEvent mDownEvent;

    // 字段用于绘制阴影下固定部分
    private GradientDrawable mShadowDrawable;
    private int mSectionsDistanceY;
    private int mShadowHeight;

    /**
     * 委托侦听器,可以空.
     */
    OnScrollListener mDelegateOnScrollListener;

    /**
     * 影子被回收,可以为空.
     */
    PinnedSection mRecycleSection;

    /**
     * 影子与固定视图实例,可以为空。
     */
    PinnedSection mPinnedSection;

    /**
     * Y-translation固定视图。我们用它来固定在下一节。
     */
    int mTranslateY;

    /**
     * Scroll listener which does the magic
     */
    private final OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            // 得到预期的适配器
            ListAdapter adapter = getAdapter();
            if (adapter == null || visibleItemCount == 0) return; // nothing to do

            final boolean isFirstVisibleItemSection =
                    isItemViewTypePinned(adapter, adapter.getItemViewType(firstVisibleItem));

            if (isFirstVisibleItemSection) {
                View sectionView = getChildAt(0);
                if (sectionView.getTop() == getPaddingTop()) { // 观点坚持,不需要固定的影子
                    destroyPinnedShadow();
                } else { // 节不坚持顶部,确保我们有一个固定的影子
                    ensureShadowForPosition(firstVisibleItem, firstVisibleItem, visibleItemCount);
                }

            } else { // 部分不是在第一个显眼的位置
                int sectionPosition = findCurrentSectionPosition(firstVisibleItem);
                if (sectionPosition > -1) { // 我们有部分位置
                    ensureShadowForPosition(sectionPosition, firstVisibleItem, visibleItemCount);
                } else { // 没有第一项,可见部分破坏的影子
                    destroyPinnedShadow();
                }
            }
        }

        ;

    };

    /**
     * 默认改变观察者。
     */
    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            recreatePinnedShadow();
        }

        ;

        @Override
        public void onInvalidated() {
            recreatePinnedShadow();
        }
    };

    //-- 构造函数

    public PinnedSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PinnedSectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setOnScrollListener(mOnScrollListener);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        initShadow(true);
    }

    //--公共API方法

    public void setShadowVisible(boolean visible) {
        initShadow(visible);
        if (mPinnedSection != null) {
            View v = mPinnedSection.view;
            invalidate(v.getLeft(), v.getTop(), v.getRight(), v.getBottom() + mShadowHeight);
        }
    }

    //-- 固定剖面图的方法

    public void initShadow(boolean visible) {
        if (visible) {
            if (mShadowDrawable == null) {
                mShadowDrawable = new GradientDrawable(Orientation.TOP_BOTTOM,
                        new int[]{Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")});
                mShadowHeight = (int) (8 * getResources().getDisplayMetrics().density);
            }
        } else {
            if (mShadowDrawable != null) {
                mShadowDrawable = null;
                mShadowHeight = 0;
            }
        }
    }

    /**
     * 与固定视图的视图创建阴影包装在给定的位置
     */
    void createPinnedShadow(int position) {

        // 尽量回收的影子
        PinnedSection pinnedShadow = mRecycleSection;
        mRecycleSection = null;

        // 创建新的阴影
        if (pinnedShadow == null) pinnedShadow = new PinnedSection();
        // 如果这样,请求新视图使用回收的视图
        View pinnedView = getAdapter().getView(position, pinnedShadow.view, PinnedSectionListView.this);

        //读取布局参数
        ViewGroup.LayoutParams layoutParams = pinnedView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = generateDefaultLayoutParams();
            layoutParams.height = 100;
            pinnedView.setLayoutParams(layoutParams);
        }

        int heightMode = MeasureSpec.getMode(layoutParams.height);
        int heightSize = MeasureSpec.getSize(layoutParams.height);

        if (heightMode == MeasureSpec.UNSPECIFIED) heightMode = MeasureSpec.EXACTLY;

        int maxHeight = getHeight() - getListPaddingTop() - getListPaddingBottom();
        if (heightSize > maxHeight) heightSize = maxHeight;

        // 测量和布局
        int ws = MeasureSpec.makeMeasureSpec(getWidth() - getListPaddingLeft() - getListPaddingRight(), MeasureSpec.EXACTLY);
        int hs = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        pinnedView.measure(ws, hs);
        pinnedView.layout(0, 0, pinnedView.getMeasuredWidth(), pinnedView.getMeasuredHeight()-10);
        mTranslateY = 0;

        // 初始化固定的影子
        pinnedShadow.view = pinnedView;
        pinnedShadow.position = position;
        pinnedShadow.id = getAdapter().getItemId(position);

        // 存储固定的影子
        mPinnedSection = pinnedShadow;
    }

    /**
     * 销毁阴影包装目前固定视图
     */
    void destroyPinnedShadow() {
        if (mPinnedSection != null) {
            // 以后让影子被回收
            mRecycleSection = mPinnedSection;
            mPinnedSection = null;
        }
    }

    /**
     * 确保我们有一个实际固定阴影位置。
     */
    void ensureShadowForPosition(int sectionPosition, int firstVisibleItem, int visibleItemCount) {
        if (visibleItemCount < 2) { // 不需要创建的影子,我们有一个可见的项目
            destroyPinnedShadow();
            return;
        }

        if (mPinnedSection != null
                && mPinnedSection.position != sectionPosition) { // 无效的影子,如果需要的话
            destroyPinnedShadow();
        }

        if (mPinnedSection == null) { // 创建阴影,如果是空的
            createPinnedShadow(sectionPosition);
        }

        // 根据下一节的位置对齐的影子,如果必要的
        int nextPosition = sectionPosition + 1;
        if (nextPosition < getCount()) {
            int nextSectionPosition = findFirstVisibleSectionPosition(nextPosition,
                    visibleItemCount - (nextPosition - firstVisibleItem));
            if (nextSectionPosition > -1) {
                View nextSectionView = getChildAt(nextSectionPosition - firstVisibleItem);
                final int bottom = mPinnedSection.view.getBottom() + getPaddingTop();
                mSectionsDistanceY = nextSectionView.getTop() - bottom;
                if (mSectionsDistanceY < 0) {
                    // 下一部分固定的影子重叠,移动它
                    mTranslateY = mSectionsDistanceY;
                } else {
                    // 下一节与固定不重叠,坚持
                    mTranslateY = 0;
                }
            } else {
                // 没有其他部分是可见的,坚持
                mTranslateY = 0;
                mSectionsDistanceY = Integer.MAX_VALUE;
            }
        }

    }

    int findFirstVisibleSectionPosition(int firstVisibleItem, int visibleItemCount) {
        ListAdapter adapter = getAdapter();

        int adapterDataCount = adapter.getCount();
        if (getLastVisiblePosition() >= adapterDataCount) return -1; // 数据已经改变,没有候选人

        if (firstVisibleItem + visibleItemCount >= adapterDataCount) {//添加到防止指数Outofbound(案例)
            visibleItemCount = adapterDataCount - firstVisibleItem;
        }

        for (int childIndex = 0; childIndex < visibleItemCount; childIndex++) {
            int position = firstVisibleItem + childIndex;
            int viewType = adapter.getItemViewType(position);
            if (isItemViewTypePinned(adapter, viewType)) return position;
        }
        return -1;
    }

    int findCurrentSectionPosition(int fromPosition) {
        ListAdapter adapter = getAdapter();

        if (fromPosition >= adapter.getCount()) return -1; //数据已经改变,没有候选人

        if (adapter instanceof SectionIndexer) {
            // 尽量快的方式通过询问部分索引器
            SectionIndexer indexer = (SectionIndexer) adapter;
            int sectionPosition = indexer.getSectionForPosition(fromPosition);
            int itemPosition = indexer.getPositionForSection(sectionPosition);
            int typeView = adapter.getItemViewType(itemPosition);
            if (isItemViewTypePinned(adapter, typeView)) {
                return itemPosition;
            } // else, no luck
        }

        // 尝试缓慢的方式通过下一节项以上
        for (int position = fromPosition; position >= 0; position--) {
            int viewType = adapter.getItemViewType(position);
            if (isItemViewTypePinned(adapter, viewType)) return position;
        }
        return -1; // no candidate found
    }

    void recreatePinnedShadow() {
        destroyPinnedShadow();
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter.getCount() > 0) {
            int firstVisiblePosition = getFirstVisiblePosition();
            int sectionPosition = findCurrentSectionPosition(firstVisiblePosition);
            if (sectionPosition == -1) return; // no views to pin, exit
            ensureShadowForPosition(sectionPosition,
                    firstVisiblePosition, getLastVisiblePosition() - firstVisiblePosition);
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        if (listener == mOnScrollListener) {
            super.setOnScrollListener(listener);
        } else {
            mDelegateOnScrollListener = listener;
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() {
            @Override
            public void run() { //配置更改后还原固定视图
                recreatePinnedShadow();
            }
        });
    }

    @Override
    public void setAdapter(ListAdapter adapter) {

        // 断言在调试模式下适配器
        if (adapter != null) {
            if (!(adapter instanceof PinnedSectionListAdapter))
                throw new IllegalArgumentException("Does your adapter implement PinnedSectionListAdapter?");
            if (adapter.getViewTypeCount() < 2)
                throw new IllegalArgumentException("Does your adapter handle at least two types" +
                        " of views in getViewTypeCount() method: items and sections?");
        }

        //注销观察者在老适配器和注册新的
        ListAdapter oldAdapter = getAdapter();
        if (oldAdapter != null) oldAdapter.unregisterDataSetObserver(mDataSetObserver);
        if (adapter != null) adapter.registerDataSetObserver(mDataSetObserver);

        // 破坏固定的影子,如果新适配器不是旧的一样
        if (oldAdapter != adapter) destroyPinnedShadow();

        super.setAdapter(adapter);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mPinnedSection != null) {
            int parentWidth = r - l - getPaddingLeft() - getPaddingRight();
            int shadowWidth = mPinnedSection.view.getWidth();
            if (parentWidth != shadowWidth) {
                recreatePinnedShadow();
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mPinnedSection != null) {

            // 准备变量
            int pLeft = getListPaddingLeft();
            int pTop = getListPaddingTop();
            View view = mPinnedSection.view;

            // 保存画布
            canvas.save();

            int clipHeight = view.getHeight() +
                    (mShadowDrawable == null ? 0 : Math.min(mShadowHeight, mSectionsDistanceY));
            canvas.clipRect(pLeft, pTop, pLeft + view.getWidth(), pTop + clipHeight);

            canvas.translate(pLeft, pTop + mTranslateY);
            drawChild(canvas, mPinnedSection.view, getDrawingTime());

            if (mShadowDrawable != null && mSectionsDistanceY > 0) {
                mShadowDrawable.setBounds(mPinnedSection.view.getLeft(),
                        mPinnedSection.view.getBottom(),
                        mPinnedSection.view.getRight(),
                        mPinnedSection.view.getBottom() + mShadowHeight);
                mShadowDrawable.draw(canvas);
            }

            canvas.restore();
        }
    }

    //-- 触摸处理方法

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        final float x = ev.getX();
        final float y = ev.getY();
        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN
                && mTouchTarget == null
                && mPinnedSection != null
                && isPinnedViewTouched(mPinnedSection.view, x, y)) { // 创建联系目标

            // 用户接触固定视图
            mTouchTarget = mPinnedSection.view;
            mTouchPoint.x = x;
            mTouchPoint.y = y;

            // 事件最终被使用后复制下来
            mDownEvent = MotionEvent.obtain(ev);
        }

        if (mTouchTarget != null) {
            if (isPinnedViewTouched(mTouchTarget, x, y)) { // 事件转发给固定的看法
                mTouchTarget.dispatchTouchEvent(ev);
            }

            if (action == MotionEvent.ACTION_UP) { // 对固定视图执行onClick
                super.dispatchTouchEvent(ev);
                performPinnedItemClick();
                clearTouchTarget();

            } else if (action == MotionEvent.ACTION_CANCEL) { // cancel
                clearTouchTarget();

            } else if (action == MotionEvent.ACTION_MOVE) {
                if (Math.abs(y - mTouchPoint.y) > mTouchSlop) {

                    // 取消对触摸目标序列
                    MotionEvent event = MotionEvent.obtain(ev);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    mTouchTarget.dispatchTouchEvent(event);
                    event.recycle();

                    // 提供正确的序列超类作进一步处理
                    super.dispatchTouchEvent(mDownEvent);
                    super.dispatchTouchEvent(ev);
                    clearTouchTarget();

                }
            }

            return true;
        }

        // 如果这不是我们的固定视图调用super
        return super.dispatchTouchEvent(ev);
    }

    private boolean isPinnedViewTouched(View view, float x, float y) {
        view.getHitRect(mTouchRect);

        // 通过录制顶部或底部填充、执行边境点击列表项
        // 我们不添加顶部填充来保持一致的行为。
        mTouchRect.top += mTranslateY;

        mTouchRect.bottom += mTranslateY + getPaddingTop();
        mTouchRect.left += getPaddingLeft();
        mTouchRect.right -= getPaddingRight();
        return mTouchRect.contains((int) x, (int) y);
    }

    private void clearTouchTarget() {
        mTouchTarget = null;
        if (mDownEvent != null) {
            mDownEvent.recycle();
            mDownEvent = null;
        }
    }

    private boolean performPinnedItemClick() {
        if (mPinnedSection == null) return false;

        OnItemClickListener listener = getOnItemClickListener();
        if (listener != null && getAdapter().isEnabled(mPinnedSection.position)) {
            View view = mPinnedSection.view;
            playSoundEffect(SoundEffectConstants.CLICK);
            if (view != null) {
                view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
            }
            listener.onItemClick(this, view, mPinnedSection.position, mPinnedSection.id);
            return true;
        }
        return false;
    }

    public static boolean isItemViewTypePinned(ListAdapter adapter, int viewType) {
        if (adapter instanceof HeaderViewListAdapter) {
            adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        return ((PinnedSectionListAdapter) adapter).isItemViewTypePinned(viewType);
    }

}
