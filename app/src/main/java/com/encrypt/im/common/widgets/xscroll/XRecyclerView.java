package com.encrypt.im.common.widgets.xscroll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by v_zhaoqingfa on 2018/12/21.
 */

public class XRecyclerView extends RecyclerView{

//    private WrapAdapter mWrapAdapter;

    private float mLastY = -1;
    public XRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public XRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

    }


//    @Override
//    public void setAdapter(@Nullable Adapter adapter) {
//        mWrapAdapter = new WrapAdapter(adapter);
//        super.setAdapter(mWrapAdapter);
////        adapter.registerAdapterDataObserver(mDataObserver);
////        mDataObserver.onChanged();
//
//    }
//
//    // 避免用户自己调用getAdapter() 引起的ClassCastException
//    @Nullable
//    @Override
//    public Adapter getAdapter() {
//        if(mWrapAdapter != null)
//            return mWrapAdapter.getOriginalAdapter();
//        else
//            return null;
//    }
//
//    @Override
//    public void setLayoutManager(@Nullable LayoutManager layout) {
//        super.setLayoutManager(layout);
//        if(mWrapAdapter != null){
//            if (layout instanceof GridLayoutManager) {
//                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
//                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return (mWrapAdapter.isHeader(position) || mWrapAdapter.isFooter(position)
//                                || mWrapAdapter.isRefreshHeader(position))
//                                ? gridManager.getSpanCount() : 1;
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (mLastY == -1) {
//            mLastY = ev.getRawY();
//        }
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastY = ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float deltaY = ev.getRawY() - mLastY;
//                mLastY = ev.getRawY();
////                if (isOnTop() && pullRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
////                    if(mRefreshHeader == null)
////                        break;
////                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
////                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
////                        return false;
////                    }
////                }
//                break;
//            default:
//                mLastY = -1; // reset
////                if (isOnTop() && pullRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
////                    if (mRefreshHeader != null && mRefreshHeader.releaseAction()) {
////                        if (mLoadingListener != null) {
////                            mLoadingListener.onRefresh();
////                        }
////                    }
////                }
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }
//
//    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        private RecyclerView.Adapter adapter;
//
//        public WrapAdapter(RecyclerView.Adapter adapter) {
//            this.adapter = adapter;
//        }
//
//        public RecyclerView.Adapter getOriginalAdapter(){
//            return this.adapter;
//        }
//
//        public boolean isHeader(int position) {
////            if(mHeaderViews == null)
////                return false;
////            return position >= 1 && position < mHeaderViews.size() + 1;
//        }
//
//        public boolean isFooter(int position) {
//            if(loadingMoreEnabled) {
//                return position == getItemCount() - 1;
//            }else {
//                return false;
//            }
//        }
//
//        public boolean isRefreshHeader(int position) {
//            return position == 0;
//        }
//
//        public int getHeadersCount() {
//            if(mHeaderViews == null)
//                return 0;
//            return mHeaderViews.size();
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_REFRESH_HEADER) {
//                return new SimpleViewHolder(mRefreshHeader);
//            } else if (isHeaderType(viewType)) {
//                return new SimpleViewHolder(getHeaderViewByType(viewType));
//            } else if (viewType == TYPE_FOOTER) {
//                return new SimpleViewHolder(mFootView);
//            }
//            return adapter.onCreateViewHolder(parent, viewType);
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if (isHeader(position) || isRefreshHeader(position)) {
//                return;
//            }
//            int adjPosition = position - (getHeadersCount() + 1);
//            int adapterCount;
//            if (adapter != null) {
//                adapterCount = adapter.getItemCount();
//                if (adjPosition < adapterCount) {
//                    adapter.onBindViewHolder(holder, adjPosition);
//                }
//            }
//        }
//
//        // some times we need to override this
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,List<Object> payloads) {
//            if (isHeader(position) || isRefreshHeader(position)) {
//                return;
//            }
//
//            int adjPosition = position - (getHeadersCount() + 1);
//            int adapterCount;
//            if (adapter != null) {
//                adapterCount = adapter.getItemCount();
//                if (adjPosition < adapterCount) {
//                    if(payloads.isEmpty()){
//                        adapter.onBindViewHolder(holder, adjPosition);
//                    }
//                    else{
//                        adapter.onBindViewHolder(holder, adjPosition,payloads);
//                    }
//                }
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            int adjLen = (loadingMoreEnabled?2:1);
//            if (adapter != null) {
//                return getHeadersCount() + adapter.getItemCount() + adjLen;
//            } else {
//                return getHeadersCount() + adjLen;
//            }
//        }
//
//        @Override
//        public int getItemViewType(int position) {
////            int adjPosition = position - (getHeadersCount() + 1);
////            if (isRefreshHeader(position)) {
////                return TYPE_REFRESH_HEADER;
////            }
////            if (isHeader(position)) {
////                position = position - 1;
////                return sHeaderTypes.get(position);
////            }
////            if (isFooter(position)) {
////                return TYPE_FOOTER;
////            }
//            int adapterCount;
//            if (adapter != null) {
//                adapterCount = adapter.getItemCount();
//                if (adjPosition < adapterCount) {
//                    int type =  adapter.getItemViewType(adjPosition);
//                    if(isReservedItemViewType(type)) {
//                        throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 " );
//                    }
//                    return type;
//                }
//            }
//            return 0;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            if (adapter != null && position >= getHeadersCount() + 1) {
//                int adjPosition = position - (getHeadersCount() + 1);
//                if (adjPosition < adapter.getItemCount()) {
//                    return adapter.getItemId(adjPosition);
//                }
//            }
//            return -1;
//        }
//
//        @Override
//        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//            super.onAttachedToRecyclerView(recyclerView);
//            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//            if (manager instanceof GridLayoutManager) {
//                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
//                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
//                                ? gridManager.getSpanCount() : 1;
//                    }
//                });
//            }
//            adapter.onAttachedToRecyclerView(recyclerView);
//        }
//
//        @Override
//        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//            adapter.onDetachedFromRecyclerView(recyclerView);
//        }
//
//        @Override
//        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
//            super.onViewAttachedToWindow(holder);
//            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//            if (lp != null
//                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
//                    && (isHeader(holder.getLayoutPosition()) ||isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
//                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//                p.setFullSpan(true);
//            }
//            adapter.onViewAttachedToWindow(holder);
//        }
//
//        @Override
//        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
//            adapter.onViewDetachedFromWindow(holder);
//        }
//
//        @Override
//        public void onViewRecycled(RecyclerView.ViewHolder holder) {
//            adapter.onViewRecycled(holder);
//        }
//
//        @Override
//        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
//            return adapter.onFailedToRecycleView(holder);
//        }
//
//        @Override
//        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
//            adapter.unregisterAdapterDataObserver(observer);
//        }
//
//        @Override
//        public void registerAdapterDataObserver(AdapterDataObserver observer) {
//            adapter.registerAdapterDataObserver(observer);
//        }
//
//        private class SimpleViewHolder extends RecyclerView.ViewHolder {
//            public SimpleViewHolder(View itemView) {
//                super(itemView);
//            }
//        }
//    }
}
